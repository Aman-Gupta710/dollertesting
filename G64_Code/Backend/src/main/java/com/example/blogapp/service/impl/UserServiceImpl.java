package com.example.blogapp.service.impl;

import com.example.blogapp.config.AppConstants;
import com.example.blogapp.entity.PasswordResetToken;
import com.example.blogapp.entity.Role;
import com.example.blogapp.entity.User;
import com.example.blogapp.entity.VerificationToken;
import com.example.blogapp.event.RegistrationCompleteEvent;
import com.example.blogapp.exception.InvalidEmailException;
import com.example.blogapp.exception.ResourceExistException;
import com.example.blogapp.exception.ResourceNotFoundException;
import com.example.blogapp.exception.ResourceNotMatchedException;
import com.example.blogapp.payloads.UserDto;
import com.example.blogapp.repository.*;
import com.example.blogapp.service.SubscriberService;
import com.example.blogapp.service.UserService;
import com.example.blogapp.util.EmailValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public User registerUser(UserDto userDto, HttpServletRequest request) {

        // validate email
        boolean isValidEmail = emailValidator.validateEmail(userDto.getEmail());
        if (!isValidEmail)
            throw new InvalidEmailException("Invalid email");

        // User already exist or not
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalUser.isEmpty()) {
            // Adding new user
            // checking password and matching password
            if (!userDto.getPassword().equals(userDto.getMatchingPassword()))
                throw new ResourceNotMatchedException("Password", "Matching Password");

            // create new user
            User user = this.modelMapper.map(userDto, User.class);

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Role role = roleRepository.findById(AppConstants.ROLE_USER).get();
            user.getRoles().add(role);

            return this.userRepository.save(user);
        } else {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new ResourceExistException("User", "email", userDto.getEmail());
            } else {
                // User exist but not enabled, so send verification token to enable the user
                // send verification token
                // email sent through controller
                return user;
            }
        }
    }
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepository.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());

        user = userRepository.save(user);
        return this.userToDto(user);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        this.userRepository.delete(user);
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);

    }

    @Override
    public String validateVerificationToken(String token, HttpServletRequest request) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null)
            return "invalid";

        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if(user.isEnabled())
            throw new IllegalStateException("User Already Verified");

        if (verificationToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            verificationTokenRepository.delete(verificationToken);
            publisher.publishEvent(new RegistrationCompleteEvent(user, getApplicationUrl(request)));
            return "expired";
        }

        user.setEnabled(true);
        user = userRepository.save(user);
        subscriberService.saveSubscriber(user);

        return "valid";
    }

    @Override
    public User findUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

//        verificationTokenRepository.deleteAllByUser(user.getId());
        return user;
    }

    @Override
    public void savePasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null)
            return "invalid";

        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();

        if (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    private User dtoToUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);

//        user.setId(userDto.getId());
//        user.setName(userDto.getName());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(userDto.getPassword());
//        user.setAbout(userDto.getAbout());

        return user;
    }

    public UserDto userToDto(User user) {
        UserDto userDto = this.modelMapper.map(user, UserDto.class);

//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setName(user.getName());
//        userDto.setEmail(user.getEmail());
//        userDto.setPassword(user.getPassword());
//        userDto.setAbout(user.getAbout());

        return userDto;
    }

    private String getApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() +
                ":" + request.getServerPort() + request.getContextPath();
    }
}
