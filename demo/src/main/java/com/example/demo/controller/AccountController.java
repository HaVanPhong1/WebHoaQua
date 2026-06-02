package com.example.demo.controller;
import com.example.demo.entity.AccountRole;
import com.example.demo.entity.UserAccount;
import com.example.demo.service.AccountService;


import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        return "accounts";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("account", new UserAccount());
        model.addAttribute("roles", AccountRole.values());
        return "account-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<UserAccount> account = accountService.findById(id);
        if (account.isEmpty()) {
            return "redirect:/admin/accounts";
        }
        model.addAttribute("account", account.get());
        model.addAttribute("roles", AccountRole.values());
        return "account-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute UserAccount account) {
        accountService.save(account);
        return "redirect:/admin/accounts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        accountService.deleteById(id);
        return "redirect:/admin/accounts";
    }
}
