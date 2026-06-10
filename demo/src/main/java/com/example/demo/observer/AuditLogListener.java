package com.example.demo.observer;

import com.example.demo.entity.UserAccount;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener {

    @EventListener
    public void handleAccountCreated(AccountCreatedEvent event) {
        UserAccount account = event.account();
        // Thực tế có thể ghi log này vào file hệ thống hoặc lưu vào bảng dữ liệu lưu trữ lịch sử hoạt động (System Log)
        System.out.println("[AUDIT SYSTEM LOG] Tài khoản '" + account.getUsername()
                + "' với ID [" + account.getId() + "] đã tạo thành công từ nguồn: " + event.registrationSource());
    }
}