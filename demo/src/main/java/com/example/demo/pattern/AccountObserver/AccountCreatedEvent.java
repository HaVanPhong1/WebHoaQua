package com.example.demo.pattern.AccountObserver;

import com.example.demo.entity.UserAccount;

public record AccountCreatedEvent(UserAccount account, String registrationSource) {}