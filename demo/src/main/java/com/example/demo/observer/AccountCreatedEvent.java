package com.example.demo.observer;

import com.example.demo.entity.UserAccount;

public record AccountCreatedEvent(UserAccount account, String registrationSource) {}