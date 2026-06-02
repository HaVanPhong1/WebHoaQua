package com.example.demo.controller;
import com.example.demo.entity.AccountRole;
import com.example.demo.entity.Customer;
import com.example.demo.entity.UserAccount;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.service.AccountService;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final CustomerRepository customerRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountService accountService;

    public RegistrationController(CustomerRepository customerRepository,
                                  UserAccountRepository userAccountRepository,
                                  AccountService accountService) {
        this.customerRepository = customerRepository;
        this.userAccountRepository = userAccountRepository;
        this.accountService = accountService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "register";
    }

    @PostMapping("/save")
    @Transactional
    public String registerCustomer(
            @ModelAttribute Customer customer,
            @RequestParam String username,
            @RequestParam String password,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validate input
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
        if (userAccountRepository.existsByEmail(customer.getEmail())) {
            model.addAttribute("error", "Email đã được sử dụng bởi tài khoản khác.");
            return "register";
        }

        try {
            // 1. Save customer
            customer.setActive(true);
            customerRepository.save(customer);

            // 2. Save user account linked to the customer (password will be BCrypt-encoded inside accountService.save)
            UserAccount account = new UserAccount();
            account.setUsername(username);
            account.setPassword(password);
            account.setFullName(customer.getName());
            account.setEmail(customer.getEmail());
            account.setRole(AccountRole.ROLE_CUSTOMER);
            account.setActive(true);
            accountService.save(account);

        } catch (Exception e) {
            model.addAttribute("customer", customer);
            model.addAttribute("error", "Đăng ký không thành công: " + e.getMessage());
            return "register";
        }

        redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập để tiếp tục mua hàng.");
        return "redirect:/login";
    }
}
