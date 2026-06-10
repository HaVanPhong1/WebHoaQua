package com.example.demo.pattern.strategy;
import com.example.demo.dto.ReportDataDto;
import com.example.demo.repository.ShopOrderRepository;
import com.example.demo.repository.OrderItemRepository;
/**
 * STRATEGY PATTERN – Interface chiến lược
 *
 * Định nghĩa một "hợp đồng" chung cho tất cả các chiến lược thống kê.
 * Mỗi chiến lược (Daily, Monthly, Yearly) sẽ implement interface này
 * và cung cấp cách tính/lấy dữ liệu khác nhau.
 *
 * Lợi ích: Controller không cần biết cách tính cụ thể,
 * chỉ cần gọi execute() → dễ mở rộng thêm chiến lược mới mà không sửa code cũ.
 */
public interface ReportStrategy {
    /**
     * Thực thi chiến lược thống kê và trả về dữ liệu báo cáo
     *
     * @param orderRepo     Repository truy vấn đơn hàng
     * @param itemRepo      Repository truy vấn chi tiết đơn hàng
     * @return              DTO chứa toàn bộ dữ liệu cho biểu đồ và bảng
     */
    ReportDataDto execute(ShopOrderRepository orderRepo, OrderItemRepository itemRepo);
    /**
     * Trả về tên loại báo cáo để hiển thị trên UI
     */
    String getTypeName();
}
