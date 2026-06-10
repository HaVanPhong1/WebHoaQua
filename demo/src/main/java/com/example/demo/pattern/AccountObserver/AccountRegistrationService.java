
package com.example.demo.pattern.AccountObserver;

import com.example.demo.entity.UserAccount;
import com.example.demo.pattern.factory.AccountFactory;
import com.example.demo.pattern.factory.AccountFactory;
import com.example.demo.pattern.factory.AccountRegistrationDTO;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("accountObserverRegistrationService")
public class AccountRegistrationService {

    private final List<AccountFactory> factories;
    private final ApplicationEventPublisher eventPublisher;

    // Spring Boot sẽ tự động thu thập toàn bộ các Bean thực thi giao diện AccountFactory vào List này
    public AccountRegistrationService(List<AccountFactory> factories, ApplicationEventPublisher eventPublisher) {
        this.factories = factories;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UserAccount register(AccountRegistrationDTO dto) {
        // Tìm kiếm nhà máy phù hợp dựa theo Vai trò yêu cầu
        AccountFactory selectedFactory = factories.stream()
                .filter(f -> f.getSupportedRole() == dto.role() ||
                        (dto.role() == com.example.demo.entity.AccountRole.ROLE_MANAGER && f.getSupportedRole() == com.example.demo.entity.AccountRole.ROLE_ADMIN))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hệ thống không hỗ trợ khởi tạo vai trò: " + dto.role()));

        // Thực hiện tạo tài khoản qua Factory tương ứng
        UserAccount newAccount = selectedFactory.createAccount(dto);

        // PHÁT SỰ KIỆN: Thông báo cho tất cả các Observers lắng nghe hệ thống
        eventPublisher.publishEvent(new AccountCreatedEvent(newAccount, "Hệ Thống Đăng Ký Trực Tuyến"));

        return newAccount;
    }
}