package com.example.demo.service;
import com.example.demo.dto.ReportDataDto;
import com.example.demo.pattern.observer.ReportAuditObserver;
import com.example.demo.pattern.observer.ReportEventPublisher;
import com.example.demo.pattern.strategy.ReportContext;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ReportAuditLogRepository;
import com.example.demo.repository.ShopOrderRepository;
import org.springframework.stereotype.Service;
/**
 * ReportService – Kết hợp Strategy Pattern và Observer Pattern
 *
 * ┌─────────────────────────────────────────────────────┐
 * │  Strategy Pattern:                                  │
 * │  ReportContext.setStrategy(type) → execute()        │
 * │  → chọn Daily / Monthly / Yearly strategy           │
 * │  → tính toán data phù hợp                          │
 * ├─────────────────────────────────────────────────────┤
 * │  Observer Pattern:                                  │
 * │  ReportEventPublisher.publish(user, type)           │
 * │  → thông báo ReportAuditObserver                   │
 * │  → ReportAuditObserver.onReportViewed()             │
 * │  → lưu audit log vào DB                            │
 * └─────────────────────────────────────────────────────┘
 */
@Service
public class ReportService {
    private final ShopOrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final ReportAuditLogRepository auditLogRepo;
    // Publisher cho Observer Pattern (được khởi tạo một lần và giữ nguyên)
    private final ReportEventPublisher publisher;
    public ReportService(ShopOrderRepository orderRepo,
                         OrderItemRepository itemRepo,
                         ReportAuditLogRepository auditLogRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
        this.auditLogRepo = auditLogRepo;
        // Khởi tạo Observer Pattern:
        // Tạo Publisher và đăng ký Observer vào
        this.publisher = new ReportEventPublisher();
        this.publisher.subscribe(new ReportAuditObserver(auditLogRepo));
    }
    /**
     * Tạo dữ liệu báo cáo theo loại được chọn.
     *
     * Luồng xử lý:
     * 1. [Strategy] ReportContext chọn strategy phù hợp (Daily/Monthly/Yearly)
     * 2. [Strategy] Thực thi strategy → tính toán dữ liệu
     * 3. [Observer] Publish sự kiện → ReportAuditObserver ghi log vào DB
     *
     * @param type     "daily" | "monthly" | "yearly"
     * @param username Tên người dùng đang xem (để ghi audit log)
     * @return         DTO chứa đầy đủ dữ liệu cho view
     */
    public ReportDataDto buildReport(String type, String username) {
        // ======= STRATEGY PATTERN =======
        // Context chọn strategy phù hợp và thực thi
        ReportContext context = new ReportContext();
        context.setStrategy(type);
        ReportDataDto data = context.execute(orderRepo, itemRepo);
        // ======= OBSERVER PATTERN =======
        // Publish sự kiện → tất cả Observer được thông báo
        // (hiện tại: ReportAuditObserver sẽ ghi log vào DB)
        publisher.publish(username, type);
        return data;
    }
}
