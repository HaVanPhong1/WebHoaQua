package com.example.demo.pattern.memento;

import com.example.demo.entity.UserAccount;
import org.springframework.stereotype.Service;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountHistoryManager {

    private final ConcurrentHashMap<Long, Stack<AccountSnapshot>> undoMap = new ConcurrentHashMap<>();

    public void saveSnapshot(UserAccount account) {
        undoMap.putIfAbsent(account.getId(), new Stack<>());

        // Gọi snapshot và ép kiểu về đúng class nằm trong package pattern.memento
        Object generatedSnapshot = account.createSnapshot();
        if (generatedSnapshot instanceof AccountSnapshot snapshot) {
            undoMap.get(account.getId()).push(snapshot);
        } else {
            // Trường hợp createSnapshot trả về kiểu cũ, thực hiện chuyển đổi thủ công
            try {
                // Sử dụng Reflection hoặc tạo mới trực tiếp để tương thích
                AccountSnapshot patternSnapshot = new AccountSnapshot(
                        account.getId(),
                        account.getEmail(),
                        account.getFullName(),
                        account.getRole(),
                        account.isActive()
                );
                undoMap.get(account.getId()).push(patternSnapshot);
            } catch (Exception e) {
                System.err.println("Lỗi đồng bộ Snapshot: " + e.getMessage());
            }
        }
    }

    public AccountSnapshot popLastSnapshot(Long accountId) {
        Stack<AccountSnapshot> stack = undoMap.get(accountId);
        if (stack != null && !stack.isEmpty()) {
            return stack.pop();
        }
        return null;
    }
}