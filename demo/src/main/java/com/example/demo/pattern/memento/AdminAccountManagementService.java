package com.example.demo.pattern.memento;

import com.example.demo.entity.AccountRole;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAccountManagementService {

    private final UserAccountRepository userAccountRepository;
    private final AccountHistoryManager historyManager;

    public AdminAccountManagementService(UserAccountRepository userAccountRepository, AccountHistoryManager historyManager) {
        this.userAccountRepository = userAccountRepository;
        this.historyManager = historyManager;
    }

    @Transactional
    public UserAccount updateAccount(Long id, String fullName, String email, AccountRole role, boolean active) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản để cập nhật dữ liệu."));

        historyManager.saveSnapshot(account);

        account.setFullName(fullName);
        account.setEmail(email);
        account.setRole(role);
        account.setActive(active);

        return userAccountRepository.save(account);
    }

    @Transactional
    public UserAccount undo(Long id) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));

        AccountSnapshot snapshot = historyManager.popLastSnapshot(id);
        if (snapshot == null) {
            throw new IllegalStateException("Không tồn tại lịch sử thay đổi để khôi phục (Undo) cho tài khoản này!");
        }

        // Đồng bộ hóa thủ công để tránh xung đột import package cũ/mới của đối tượng UserAccount
        account.setEmail(snapshot.email());
        account.setFullName(snapshot.fullName());
        account.setRole(snapshot.role());
        account.setActive(snapshot.active());

        return userAccountRepository.save(account);
    }
}