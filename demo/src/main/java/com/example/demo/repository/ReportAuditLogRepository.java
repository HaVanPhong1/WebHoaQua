package com.example.demo.repository;
import com.example.demo.entity.ReportAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.List;
/**
 * Repository cho bảng report_audit_logs.
 * Dùng để lưu và lấy lịch sử xem báo cáo (do Observer ghi vào).
 */
public interface ReportAuditLogRepository extends JpaRepository<ReportAuditLog, Long> {
    /**
     * Lấy các log mới nhất, sắp xếp theo thời gian giảm dần
     */
    List<ReportAuditLog> findTop10ByOrderByViewedAtDesc();
}
