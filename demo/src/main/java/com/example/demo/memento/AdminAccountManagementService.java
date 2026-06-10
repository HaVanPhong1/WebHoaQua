package com.example.demo.memento;

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

    /**
     * Cập nhật thông tin tài khoản (Có lưu ảnh chụp trạng thái cũ)
     */
    @Transactional
    public UserAccount updateAccount(Long id, String fullName, String email, AccountRole role, boolean active) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản để cập nhật dữ liệu."));

        // Thực hiện sao lưu trước khi thay đổi trạng thái
        historyManager.saveSnapshot(account);

        account.setFullName(fullName);
        account.setEmail(email);
        account.setRole(role);
        account.setActive(active);

        return userAccountRepository.save(account);
    }

    /**
     * Hoàn tác (Undo) hành động cập nhật gần nhất
     */
    @Transactional
    public UserAccount undo(Long id) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));

        AccountSnapshot snapshot = historyManager.popLastSnapshot(id);
        if (snapshot == null) {
            throw new IllegalStateException("Không tồn tại lịch sử thay đổi để khôi phục (Undo) cho tài khoản này!");
        }

        // Thực hiện khôi phục trạng thái thông qua ảnh chụp memento
        account.restoreFromSnapshot(snapshot);

        return userAccountRepository.save(account);
    }
}