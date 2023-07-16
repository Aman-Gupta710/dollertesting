package com.example.blogapp.service;

import com.example.blogapp.entity.User;
import com.example.blogapp.payloads.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(UserDto userDto, HttpServletRequest request);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto user, Integer userId);

    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();

    void deleteUser(Integer userId);

    void saveVerificationTokenForUser(User user, String token);

    String validateVerificationToken(String token, HttpServletRequest request);

    User findUserByEmail(String email);

    void savePasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

//    void changePassword(User user, PasswordModel passwordModel);

    void changePassword(User user, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);
}
