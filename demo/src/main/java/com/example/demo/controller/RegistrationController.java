package com.example.demo.controller;

import com.example.demo.entity.AccountRole;
import com.example.demo.entity.Customer;
import com.example.demo.factory.AccountRegistrationDTO;
import com.example.demo.observer.AccountRegistrationService;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.UserAccountRepository;
import org.springframework.stereotype.Controller;
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
    private final AccountRegistrationService accountRegistrationService;

    public RegistrationController(CustomerRepository customerRepository,
                                  UserAccountRepository userAccountRepository,
                                  AccountRegistrationService accountRegistrationService) {
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

        // --- Bước 1: Kiểm tra tính hợp lệ của dữ liệu (Validation) ---
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
            // --- Bước 2: Đóng gói dữ liệu vào DTO ---
            AccountRegistrationDTO registrationDTO = new AccountRegistrationDTO(
                    username,
                    password,
                    customer.getEmail(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getAddress(),
                    AccountRole.ROLE_CUSTOMER // Form đăng ký công khai mặc định là khách hàng mua sản phẩm
            );

            // Gửi dữ liệu xử lý tập trung qua chuỗi Design Patterns
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