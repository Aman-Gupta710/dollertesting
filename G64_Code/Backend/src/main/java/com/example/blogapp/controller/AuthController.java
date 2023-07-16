package com.example.blogapp.controller;

import com.example.blogapp.entity.User;
import com.example.blogapp.event.PasswordResetEvent;
import com.example.blogapp.event.RegistrationCompleteEvent;
import com.example.blogapp.payloads.*;
import com.example.blogapp.security.JwtHelper;
import com.example.blogapp.service.EmailService;
import com.example.blogapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws BadCredentialsException {

        this.authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());

        String token = this.jwtHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        response.setUser(this.modelMapper.map((User) userDetails, UserDto.class));

        return new ResponseEntity<JwtAuthResponse>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) throws BadCredentialsException {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        this.authenticationManager.authenticate(token);

    }

    // register user
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserDto userDto, final HttpServletRequest request) {

        System.out.println("\n" + userDto + "\n");

        User savedUser = userService.registerUser(userDto, request);
        publisher.publishEvent(new RegistrationCompleteEvent(savedUser, getFrontendApplicationUrl(request)));
        System.out.println("\n----------\n" + "Controller : " + Thread.currentThread().getName() + "\n----------");
        return new ResponseEntity<>(
                this.modelMapper.map(savedUser, UserResponse.class), HttpStatus.CREATED);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token,
                                                HttpServletRequest request) {
        String result = userService.validateVerificationToken(token, request);
        if (result.equalsIgnoreCase("valid"))
            return new ResponseEntity<>(
                    new ApiResponse("User verified successfully", true), HttpStatus.OK);

        else if (result.equalsIgnoreCase("expired"))
            return new ResponseEntity<>(
                    new ApiResponse("Link is expired. We have sent you a new link on your email. " +
                            "Please verify by clicking on the link.", true), HttpStatus.OK);
        else
            return new ResponseEntity<>(
                    new ApiResponse("Invalid link", false), HttpStatus.BAD_REQUEST);
    }

    // Resend Verification Token
    @GetMapping("/resendVerificationToken")
    public ResponseEntity<?> resendVerificationToken(@RequestParam("email") String email, final HttpServletRequest request) {
        User savedUser = userService.findUserByEmail(email);
        publisher.publishEvent(new RegistrationCompleteEvent(savedUser, getFrontendApplicationUrl(request)));
        return new ResponseEntity<>(
                new ApiResponse("New verification link sent to your email id. " +
                        "Verify account by clicking the link provided in the email", true),
                HttpStatus.OK);
    }


    // Reset Password
    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordModel passwordModel, final HttpServletRequest request){

        User user = userService.findUserByEmail(passwordModel.getEmail());

        publisher.publishEvent(new PasswordResetEvent(user, getFrontendApplicationUrl(request)));
        return new ResponseEntity<>(new ApiResponse
                ("Reset password link sent to your registered email id", true), HttpStatus.OK);
    }

    // Save new Password
    @PostMapping("/savePassword")
    @Transactional
    public ResponseEntity<?> resetPasswordVerification(@RequestParam("token") String token,
                                                       @NotNull @RequestBody PasswordModel passwordModel) {
        String result = userService.validatePasswordResetToken(token);

        if (result.equalsIgnoreCase("expired"))
            return new ResponseEntity<>(
                    new ApiResponse("Link is expired. Try again with new link.", false),
                    HttpStatus.OK);

        else if (result.equalsIgnoreCase("invalid"))
            return new ResponseEntity<>(
                    new ApiResponse("Invalid link", false),
                    HttpStatus.OK);

        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return new ResponseEntity<>(
                    new ApiResponse("Password Reset Successfully ", true),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ApiResponse("Invalid link", false),
                    HttpStatus.OK);
        }
    }

//    Change Password
    @PostMapping("/changePassword")
    @Transactional
    public ResponseEntity<?> changePassword(@RequestBody PasswordModel passwordModel) {
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (user == null)
            return new ResponseEntity<>(new ApiResponse("User Not Found", false),
                    HttpStatus.NOT_FOUND);
        if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword()))
            return new ResponseEntity<>(new ApiResponse("Invalid Old Password", false),
                    HttpStatus.BAD_REQUEST);

        // save new Password
//        if(passwordModel.getNewPassword().length() < 8)
//            return new ResponseEntity<>("Password length must be of at least 8 characters",
//                    HttpStatus.BAD_REQUEST);
        userService.changePassword(user, passwordModel.getNewPassword());
        return new ResponseEntity<>(new ApiResponse("Password changed successfully", true), HttpStatus.OK);
    }

    private String getApplicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() +
                ":" + request.getServerPort() + request.getContextPath();
    }

    private String getFrontendApplicationUrl(HttpServletRequest request) {
        String origin = request.getHeader("origin");
        System.out.println("\nFrontendUrl : " + origin + "\n");
        return origin;
    }

}
