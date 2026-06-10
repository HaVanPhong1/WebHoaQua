package com.example.demo.controller;

import com.example.demo.entity.AccountRole;
import com.example.demo.entity.Customer;
import com.example.demo.pattern.factory.AccountRegistrationDTO;
import com.example.demo.pattern.AccountObserver.AccountRegistrationService;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final CustomerRepository customerRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountRegistrationService accountRegistrationService;

    public RegistrationController(CustomerRepository customerRepository,
                                  UserAccountRepository userAccountRepository,
                                  @Qualifier("accountObserverRegistrationService") AccountRegistrationService accountRegistrationService) {
        this.customerRepository = customerRepository;
        this.userAccountRepository = userAccountRepository;
        this.accountRegistrationService = accountRegistrationService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/save")
    public String registerCustomer(
            @ModelAttribute Customer customer,
            @RequestParam String username,
            @RequestParam String password,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            model.addAttribute("error", "Email là bắt buộc.");
            return "register";
        }
        if (customer.getName() == null || customer.getName().isBlank()) {
            model.addAttribute("error", "Họ và tên là bắt buộc.");
            return "register";
        }
        if (username == null || username.isBlank()) {
            model.addAttribute("error", "Tên đăng nhập là bắt buộc.");
            return "register";
        }
        if (password == null || password.isBlank()) {
            model.addAttribute("error", "Mật khẩu là bắt buộc.");
            return "register";
        }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            model.addAttribute("error", "Email đã được sử dụng. Vui lòng dùng email khác.");
            return "register";
        }
        if (userAccountRepository.existsByUsername(username)) {
            model.addAttribute("error", "Tên đăng nhập đã được sử dụng. Vui lòng dùng tên đăng nhập khác.");
            return "register";
        }

        try {
            AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO(
                    username,
                    password,
                    customer.getEmail(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getAddress(),
                    AccountRole.ROLE_CUSTOMER
            );

            accountRegistrationService.register(registrationDTO);

        } catch (Exception e) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "Đăng ký không thành công: " + e.getMessage());
            return "register";
        }

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập để tiếp tục mua hàng.");
        return "redirect:/login";
    }
}