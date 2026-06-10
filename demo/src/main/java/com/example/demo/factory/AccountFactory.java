package com.example.demo.factory;

import com.example.demo.entity.AccountRole;
import com.example.demo.entity.UserAccount;

public interface AccountFactory {
    AccountRole getSupportedRole();
    UserAccount createAccount(AccountRegistrationDTO dto);
}