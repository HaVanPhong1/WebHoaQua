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

    /**
     * Xuất dữ liệu thống kê báo cáo ra file CSV (UTF-8 có BOM hỗ trợ tốt cho Excel)
     */
    @GetMapping("/export")
    public void exportReport(
            @RequestParam(defaultValue = "daily") String type,
            Authentication authentication,
            jakarta.servlet.http.HttpServletResponse response) throws java.io.IOException {
        
        String username = (authentication != null) ? authentication.getName() : "anonymous";
        ReportDataDto reportData = reportService.buildReport(type, username);
        
        String filename = "bao-cao-thong-ke-" + type + ".csv";
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        
        // Ghi BOM UTF-8 (EF BB BF) để Excel nhận dạng tiếng Việt có dấu
        response.getOutputStream().write(new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF });
        
        java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(response.getOutputStream(), java.nio.charset.StandardCharsets.UTF_8));
        
        writer.println("BÁO CÁO THỐNG KÊ CHI TIẾT");
        writer.println("Loại báo cáo," + (type.equals("daily") ? "Theo ngày" : type.equals("monthly") ? "Theo tháng" : "Theo năm"));
        writer.println("Người xuất," + username);
        writer.println("Thời gian xuất," + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        writer.println();
        
        writer.println("1. CHỈ SỐ TỔNG QUAN");
        writer.println("Tổng doanh thu," + reportData.getTotalRevenue() + "₫");
        writer.println("Tổng đơn hàng," + reportData.getTotalOrders());
        writer.println("Đơn đã giao," + reportData.getDeliveredOrders());
        writer.println("Đơn mới," + reportData.getNewOrders());
        writer.println("Đơn đã hủy," + reportData.getCancelledOrders());
        writer.println();
        
        writer.println("2. XU HƯỚNG DOANH THU");
        writer.println("Thời gian,Doanh thu (₫)");
        if (reportData.getRevenueLabels() != null && reportData.getRevenueValues() != null) {
            for (int i = 0; i < reportData.getRevenueLabels().size(); i++) {
                String label = reportData.getRevenueLabels().get(i);
                java.math.BigDecimal val = i < reportData.getRevenueValues().size() ? reportData.getRevenueValues().get(i) : java.math.BigDecimal.ZERO;
                writer.println(label + "," + val);
            }
        }
        writer.println();
        
        writer.println("3. TOP SẢN PHẨM BÁN CHẠY");
        writer.println("Thứ hạng,Sản phẩm,Đã bán (kg),Doanh thu (₫)");
        if (reportData.getTopProducts() != null) {
            for (int i = 0; i < reportData.getTopProducts().size(); i++) {
                com.example.demo.dto.TopProductDto p = reportData.getTopProducts().get(i);
                writer.println((i + 1) + "," + p.getProductName() + "," + p.getTotalSold() + "," + p.getTotalRevenue());
            }
        }
        writer.println();
        
        writer.println("4. DOANH THU THEO DANH MỤC");
        writer.println("Danh mục,Doanh thu (₫)");
        if (reportData.getCategoryLabels() != null && reportData.getCategoryRevenues() != null) {
            for (int i = 0; i < reportData.getCategoryLabels().size(); i++) {
                String label = reportData.getCategoryLabels().get(i);
                java.math.BigDecimal rev = i < reportData.getCategoryRevenues().size() ? reportData.getCategoryRevenues().get(i) : java.math.BigDecimal.ZERO;
                writer.println(label + "," + rev);
            }
        }
        
        writer.flush();
    }
}