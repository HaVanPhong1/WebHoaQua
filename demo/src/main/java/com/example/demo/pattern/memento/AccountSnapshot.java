package com.example.demo.pattern.memento;

import com.example.demo.entity.AccountRole;

public record AccountSnapshot(
        Long accountId,
        String email,
        String fullName,
        AccountRole role,
        boolean active
) {}