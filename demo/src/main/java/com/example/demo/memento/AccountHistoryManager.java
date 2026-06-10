package com.example.demo.memento;

import com.example.demo.entity.UserAccount;
import org.springframework.stereotype.Service;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountHistoryManager {

    // Quản lý các bước sao lưu trạng thái dựa theo từng ID tài khoản cụ thể
    private final ConcurrentHashMap<Long, Stack<AccountSnapshot>> historyMap = new ConcurrentHashMap<>();

    public void saveSnapshot(UserAccount account) {
        historyMap.putIfAbsent(account.getId(), new Stack<>());
        // Gọi hàm chụp ảnh trạng thái trên chính đối tượng UserAccount
        historyMap.get(account.getId()).push(account.createSnapshot());
    }

    public AccountSnapshot popLastSnapshot(Long accountId) {
        Stack<AccountSnapshot> accountStack = historyMap.get(accountId);
        if (accountStack != null && !accountStack.isEmpty()) {
            return accountStack.pop();
        }
        return null;
    }
}