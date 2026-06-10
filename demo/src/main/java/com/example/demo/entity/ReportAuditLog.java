package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
/**
 * Entity lưu lịch sử xem báo cáo (audit log).
 * Được Observer tự động tạo mỗi khi admin xem trang báo cáo.
 *
 * Bảng: report_audit_logs
 */
@Entity
@Table(name = "report_audit_logs")
public class ReportAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String viewedBy;       // Username của admin
    @Column(nullable = false)
    private String reportType;     // daily / monthly / yearly
    @Column(nullable = false)
    private LocalDateTime viewedAt; // Thời điểm xem
    public ReportAuditLog() {}
    public ReportAuditLog(String viewedBy, String reportType, LocalDateTime viewedAt) {
        this.viewedBy = viewedBy;
        this.reportType = reportType;
        this.viewedAt = viewedAt;
    }
    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getViewedBy() { return viewedBy; }
    public void setViewedBy(String viewedBy) { this.viewedBy = viewedBy; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public LocalDateTime getViewedAt() { return viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
    public String getFormattedViewedAt() {
        if (viewedAt == null) return "";
        return viewedAt.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    public String getReportTypeLabel() {
        return switch (reportType) {
            case "daily"   -> "Theo ngày";
            case "monthly" -> "Theo tháng";
            case "yearly"  -> "Theo năm";
            default        -> reportType;
        };
    }
}
