package com.example.blogapp.service;

import java.util.Map;

public interface EmailService {
    void send(String to, String email, String subject);

    void send(String to, Map<String, String> model, String subject);

    String buildEmail(String name, String link);
}
