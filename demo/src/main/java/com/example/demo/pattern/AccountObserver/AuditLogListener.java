package com.example.demo.pattern.AccountObserver;

import com.example.demo.entity.UserAccount;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener {

    @EventListener
    public void onAccountCreated(AccountCreatedEvent event) {
        UserAccount account = event.account();
        System.out.println("[OBSERVER - AUDIT LOG] Ghi vết hệ thống: Tài khoản mới '" + account.getUsername()
                + "' mang ID [" + account.getId() + "] tạo từ nguồn: " + event.registrationSource());
    }
}