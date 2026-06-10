package com.example.demo.controller;
import com.example.demo.dto.ReportDataDto;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReportAuditLogRepository;
import com.example.demo.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Controller cho trang Báo cáo & Thống kê.
 *
 * Sử dụng ReportService để:
 *   - [Strategy]  chọn chiến lược thống kê theo tham số ?type=daily|monthly|yearly
 *   - [Observer]  tự động ghi audit log khi admin xem báo cáo
 *
 * Controller này rất gọn gàng vì mọi logic đã được tách sang Service + Patterns.
 */
@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    private final ReportService reportService;
    private final CustomerRepository customerRepository;
    private final ReportAuditLogRepository auditLogRepository;

    public ReportController(CustomerRepository customerRepository,
                            ReportAuditLogRepository auditLogRepository,
                            ReportService reportService) {
        this.reportService = reportService;
        this.customerRepository = customerRepository;
        this.auditLogRepository = auditLogRepository;
    }
    /**
     * Hiển thị trang báo cáo.
     *
     * @param type           Loại báo cáo: "daily" (mặc định) | "monthly" | "yearly"
     * @param authentication Thông tin người dùng đang đăng nhập (để ghi audit log)
     * @param model          Spring MVC Model để truyền dữ liệu sang template
     */
    @GetMapping
    public String report(
            @RequestParam(defaultValue = "daily") String type,
            Authentication authentication,
            Model model) {
        // Lấy username của admin đang đăng nhập
        String username = (authentication != null) ? authentication.getName() : "anonymous";
        // Gọi service: Strategy tính toán data + Observer ghi log
        ReportDataDto reportData = reportService.buildReport(type, username);

        // Truyền dữ liệu vào template
        model.addAttribute("report", reportData);
        model.addAttribute("currentType", type);
        model.addAttribute("customerCount", customerRepository.count());
        // Lịch sử xem báo cáo (Observer đã ghi vào DB, lấy ra 10 gần nhất)
        model.addAttribute("auditLogs", auditLogRepository.findTop10ByOrderByViewedAtDesc());
        return "reports";
    }
}