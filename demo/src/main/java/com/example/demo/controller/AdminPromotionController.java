package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Promotion;
import com.example.demo.repository.PromotionRepository;

@Controller
@RequestMapping("/admin/promotions")
public class AdminPromotionController {

    private final PromotionRepository promotionRepository;

    public AdminPromotionController(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("promotions", promotionRepository.findAll());
        return "admin/promotions";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        return "admin/promotion-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Promotion p = promotionRepository.findById(id).orElse(new Promotion());
        model.addAttribute("promotion", p);
        return "admin/promotion-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Promotion promotion,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);
        promotionRepository.save(promotion);
        return "redirect:/admin/promotions";
    }

    @PostMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id) {
        promotionRepository.findById(id).ifPresent(p -> {
            p.setActive(!p.isActive());
            promotionRepository.save(p);
        });
        return "redirect:/admin/promotions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        promotionRepository.findById(id).ifPresent(promotionRepository::delete);
        return "redirect:/admin/promotions";
    }
}
