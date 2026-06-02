package com.example.demo.controller;
import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;


import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "customers";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            return "redirect:/admin/customers";
        }
        model.addAttribute("customer", customer.get());
        return "customer-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Customer customer) {
        customerRepository.save(customer);
        return "redirect:/admin/customers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return "redirect:/admin/customers";
    }
}
