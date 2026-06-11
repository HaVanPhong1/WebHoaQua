package com.example.demo.pattern.AccountObserver;

import com.example.demo.entity.UserAccount;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WelcomeEmailListener {

    @Async
    @EventListener
    public void onAccountCreated(AccountCreatedEvent event) {
        UserAccount account = event.account();
        System.out.println("[OBSERVER - EMAIL] Gửi thư chào mừng đến hòm thư: " + account.getEmail());
    }
}