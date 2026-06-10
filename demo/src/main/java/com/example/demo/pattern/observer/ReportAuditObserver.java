package com.example.demo.pattern.observer;
import com.example.demo.entity.ReportAuditLog;
import com.example.demo.repository.ReportAuditLogRepository;
/**
 * OBSERVER PATTERN – Concrete Observer
 *
 * ReportAuditObserver là "Observer" cụ thể, nhận sự kiện từ Publisher
 * và lưu lại lịch sử vào database (bảng report_audit_logs).
 *
 * Khi cần thêm hành động khác (ví dụ: gửi email, push notification),
 * chỉ cần tạo thêm Observer mới mà không cần sửa code hiện tại.
 */
public class ReportAuditObserver implements ReportObserver {
    private final ReportAuditLogRepository auditLogRepository;
    public ReportAuditObserver(ReportAuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    /**
     * Được gọi tự động bởi Publisher khi có sự kiện xem báo cáo.
     * Lưu thông tin vào DB.
     */
    @Override
    public void onReportViewed(ReportEvent event) {
        ReportAuditLog log = new ReportAuditLog(
                event.getViewedBy(),
                event.getReportType(),
                event.getViewedAt()
        );
        auditLogRepository.save(log);
    }
}