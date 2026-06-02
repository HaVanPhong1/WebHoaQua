package com.example.demo.service;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.UserAccountRepository;


import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final UserAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(UserAccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount save(UserAccount account) {
        if (account.getPassword() != null && !account.getPassword().isBlank()) {
            if (!account.getPassword().startsWith("$2a$")) {
                account.setPassword(passwordEncoder.encode(account.getPassword()));
            }
        }
        return accountRepository.save(account);
    }

    public Optional<UserAccount> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<UserAccount> findById(Long id) {
        return accountRepository.findById(id);
    }

    public List<UserAccount> findAll() {
        return accountRepository.findAll();
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }
}
