package com.example.blogapp.service.impl;

import com.example.blogapp.entity.Subscriber;
import com.example.blogapp.entity.User;
import com.example.blogapp.repository.SubscriberRepository;
import com.example.blogapp.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public void saveSubscriber(User user) {
        Subscriber subscriber = new Subscriber(user, user.getEmail());
        subscriberRepository.save(subscriber);
    }
}
