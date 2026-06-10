package com.example.demo.factory;

import com.example.demo.entity.AccountRole;

public record AccountRegistrationDTO(
        String username,
        String password,
        String email,
        String fullName,
        String phone,
        String address,
        AccountRole role
) {}