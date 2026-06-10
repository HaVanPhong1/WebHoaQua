package com.example.demo.factory;

import com.example.demo.entity.AccountRole;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminAccountFactory implements AccountFactory {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountFactory(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountRole getSupportedRole() {
        // Nhà máy này có khả năng xử lý tạo tài khoản cho ADMIN
        return AccountRole.ROLE_ADMIN;
    }

    @Override
    public UserAccount createAccount(AccountRegistrationDTO dto) {
        UserAccount account = new UserAccount();
        account.setUsername(dto.username());
        account.setPassword(passwordEncoder.encode(dto.password()));
        account.setEmail(dto.email());
        account.setFullName(dto.fullName());
        // Cho phép gán linh hoạt ROLE_ADMIN hoặc ROLE_MANAGER tùy thuộc vào DTO truyền lên
        account.setRole(dto.role() != null ? dto.role() : AccountRole.ROLE_ADMIN);
        account.setActive(true);

        return userAccountRepository.save(account);
    }
}