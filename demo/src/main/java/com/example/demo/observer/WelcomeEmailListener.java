package com.example.demo.observer;

import com.example.demo.entity.UserAccount;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WelcomeEmailListener {

    @Async // Chạy bất đồng bộ ở luồng xử lý khác để không làm chậm luồng đăng ký của người dùng
    @EventListener
    public void handleAccountCreated(AccountCreatedEvent event) {
        UserAccount account = event.account();
        // Giả lập logic cấu hình gửi mail thực tế
        System.out.println("[EMAIL NOTIFICATION] Đang chuẩn bị gửi mail kích hoạt gửi tới: " + account.getEmail());
        System.out.println("Nội dung: Xin chào " + account.getFullName() + ", Chào mừng bạn đã tham gia mua sắm tại FruitShop!");
    }
}