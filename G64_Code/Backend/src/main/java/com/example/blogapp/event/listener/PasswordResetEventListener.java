package com.example.blogapp.event.listener;

import com.example.blogapp.entity.User;
import com.example.blogapp.event.PasswordResetEvent;
import com.example.blogapp.service.EmailService;
import com.example.blogapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class PasswordResetEventListener implements ApplicationListener<PasswordResetEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    @Async
    public void onApplicationEvent(PasswordResetEvent event) {
        // generate verification token for user
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        userService.savePasswordResetTokenForUser(user,token);

        // create link
//        String url = event.getApplicationUrl() + "/api/v1/auth/savePassword?token=" + token;
        String url = event.getApplicationUrl() + "/forget-password/" + token;

        Map<String, String> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("link", url);
        emailService.send(user.getEmail(), model, "Reset Password");
    }
}
