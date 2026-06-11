package com.example.demo.pattern.factory;

import com.example.demo.entity.AccountRole;
import com.example.demo.entity.Customer;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomerAccountFactory implements AccountFactory {

    private final UserAccountRepository userAccountRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerAccountFactory(UserAccountRepository userAccountRepository,
                                  CustomerRepository customerRepository,
                                  PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AccountRole getSupportedRole() {
        return AccountRole.ROLE_CUSTOMER;
    }

    @Override
    public UserAccount createAccount(AccountRegistrationDTO dto) {
        // 1. Khởi tạo và lưu thông tin Khách hàng (Customer)
        Customer customer = new Customer();
        customer.setName(dto.fullName());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setAddress(dto.address());
        customer.setActive(true);
        customerRepository.save(customer);

        // 2. Khởi tạo và lưu thông tin tài khoản đăng nhập (UserAccount) tương ứng
        UserAccount account = new UserAccount();
        account.setUsername(dto.username());
        account.setPassword(passwordEncoder.encode(dto.password()));
        account.setEmail(dto.email());
        account.setFullName(dto.fullName());
        account.setRole(AccountRole.ROLE_CUSTOMER);
        account.setActive(true);

        return userAccountRepository.save(account);
    }
}