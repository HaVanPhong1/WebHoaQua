package com.example.demo.pattern.observer;
/**
 * OBSERVER PATTERN – Observer Interface
 *
 * Định nghĩa interface cho tất cả các Observer muốn lắng nghe sự kiện báo cáo.
 * Mỗi Observer phải implement phương thức onReportViewed().
 *
 * Lợi ích: Có thể thêm nhiều Observer khác nhau (ghi log, gửi email thông báo,
 * cập nhật dashboard realtime...) mà không cần sửa Publisher hay Controller.
 */
public interface ReportObserver {
    /**
     * Được gọi tự động khi sự kiện xem báo cáo xảy ra
     * @param event thông tin sự kiện (ai xem, loại báo cáo, thời điểm)
     */
    void onReportViewed(ReportEvent event);
}
