package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private AccountRole role = AccountRole.ROLE_ADMIN;

    private boolean active = true;

    public UserAccount() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // ===================================================
    // MEMENTO PATTERN - ORIGINATOR METHODS
    // ===================================================

    /**
     * Tạo một ảnh chụp (Snapshot) chứa trạng thái hiện tại của đối tượng.
     */
    public com.example.demo.memento.AccountSnapshot createSnapshot() {
        return new com.example.demo.memento.AccountSnapshot(
                this.id,
                this.email,
                this.fullName,
                this.role,
                this.active
        );
    }

    /**
     * Khôi phục trạng thái của đối tượng từ một ảnh chụp cụ thể.
     */
    public void restoreFromSnapshot(com.example.demo.memento.AccountSnapshot snapshot) {
        if (snapshot != null) {
            this.email = snapshot.email();
            this.fullName = snapshot.fullName();
            this.role = snapshot.role();
            this.active = snapshot.active();
        }
    }
}