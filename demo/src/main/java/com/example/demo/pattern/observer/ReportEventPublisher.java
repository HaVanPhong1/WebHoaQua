package com.example.demo.pattern.observer;
import java.util.ArrayList;
import java.util.List;
/**
 * OBSERVER PATTERN – Subject / Publisher
 *
 * ReportEventPublisher là "Subject" trong Observer Pattern.
 * Nó quản lý danh sách các Observer đã đăng ký và
 * thông báo cho tất cả khi có sự kiện mới.
 *
 * Được dùng như một Spring Bean (@Component thông qua ReportService)
 * để inject và sử dụng trong toàn ứng dụng.
 */
public class ReportEventPublisher {
    // Danh sách các observer đã đăng ký
    private final List<ReportObserver> observers = new ArrayList<>();
    /**
     * Đăng ký một observer mới vào danh sách lắng nghe
     */
    public void subscribe(ReportObserver observer) {
        observers.add(observer);
    }
    /**
     * Huỷ đăng ký observer
     */
    public void unsubscribe(ReportObserver observer) {
        observers.remove(observer);
    }
    /**
     * Kích hoạt sự kiện: thông báo cho TẤT CẢ observer đã đăng ký
     *
     * @param viewedBy   Username của admin đang xem báo cáo
     * @param reportType Loại báo cáo (daily/monthly/yearly)
     */
    public void publish(String viewedBy, String reportType) {
        ReportEvent event = new ReportEvent(viewedBy, reportType);
        for (ReportObserver observer : observers) {
            observer.onReportViewed(event);
        }
    }
}
