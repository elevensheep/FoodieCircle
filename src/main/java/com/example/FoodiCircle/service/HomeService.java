package com.example.FoodiCircle.service;

import org.springframework.stereotype.Service;

@Service
public class HomeService {

    public String getWelcomeMessage() {
        return "Welcome to FoodiCircle!";
    }
}
