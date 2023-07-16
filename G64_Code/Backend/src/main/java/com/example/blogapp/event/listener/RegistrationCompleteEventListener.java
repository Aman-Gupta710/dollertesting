package com.example.blogapp.event.listener;

import com.example.blogapp.entity.User;
import com.example.blogapp.event.RegistrationCompleteEvent;
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
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Override
    @Async
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        System.out.println("\n----------\n" + " Listener( mail sending) : " +
                Thread.currentThread().getName() + "\n----------");

        // generate verification token for user
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        userService.saveVerificationTokenForUser(user, token);

        // create link
        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        Map<String, String> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("link", url);
        emailService.send(user.getEmail(), model, "Verify your account");

    }
}
