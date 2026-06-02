package com.example.demo.controller;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ShopOrderRepository;


import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    private final ShopOrderRepository orderRepository;
    private final CustomerRepository customerRepository;

    public ReportController(ShopOrderRepository orderRepository, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String report(Model model) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusDays(30);

        model.addAttribute("totalRevenue", orderRepository.totalRevenueBetween(from, now));
        model.addAttribute("orderCount", orderRepository.countOrdersBetween(from, now));
        model.addAttribute("customerCount", customerRepository.count());
        return "reports";
    }
}
