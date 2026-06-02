package com.example.demo.controller;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.ShopOrder;
import com.example.demo.repository.ShopOrderRepository;


import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    private final ShopOrderRepository orderRepository;

    public OrderController(ShopOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        model.addAttribute("statuses", OrderStatus.values());
        return "orders";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Optional<ShopOrder> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", order.get());
        model.addAttribute("statuses", OrderStatus.values());
        return "order-detail";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam OrderStatus status) {
        Optional<ShopOrder> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            ShopOrder shopOrder = order.get();
            shopOrder.setStatus(status);
            orderRepository.save(shopOrder);
        }
        return "redirect:/admin/orders";
    }
}
