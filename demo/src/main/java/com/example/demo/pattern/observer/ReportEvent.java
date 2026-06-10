package com.example.demo.pattern.observer;
import java.time.LocalDateTime;
/**
 * OBSERVER PATTERN – Event Object (sự kiện)
 *
 * Đây là object mang thông tin sự kiện "admin xem báo cáo".
 * Khi admin gọi trang báo cáo, một ReportEvent sẽ được tạo ra
 * và truyền tới tất cả Observer đã đăng ký.
 */
public class ReportEvent {
    private final String viewedBy;       // Tên người xem (username admin)
    private final String reportType;     // Loại báo cáo: daily / monthly / yearly
    private final LocalDateTime viewedAt; // Thời điểm xem
    public ReportEvent(String viewedBy, String reportType) {
        this.viewedBy = viewedBy;
        this.reportType = reportType;
        this.viewedAt = LocalDateTime.now();
    }
    public String getViewedBy() {
        return viewedBy;
    }
    public String getReportType() {
        return reportType;
    }
    public LocalDateTime getViewedAt() {
        return viewedAt;
    }
}
