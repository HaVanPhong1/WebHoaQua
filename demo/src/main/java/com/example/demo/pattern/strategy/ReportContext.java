package com.example.demo.pattern.strategy;
import com.example.demo.dto.ReportDataDto;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ShopOrderRepository;
/**
 * STRATEGY PATTERN – Context Class
 *
 * Context giữ một tham chiếu tới ReportStrategy hiện đang được sử dụng.
 * Controller chỉ cần tạo ReportContext, set strategy phù hợp, rồi gọi execute().
 *
 * Điều này giúp Controller hoàn toàn độc lập với logic tính toán cụ thể
 * → tuân theo nguyên tắc Open/Closed Principle.
 */
public class ReportContext {
    private ReportStrategy strategy;
    /**
     * Khởi tạo context với strategy mặc định là Daily
     */
    public ReportContext() {
        this.strategy = new DailyReportStrategy();
    }
    /**
     * Thay đổi strategy tại runtime dựa vào tham số từ request
     * @param type "daily" | "monthly" | "yearly"
     */
    public void setStrategy(String type) {
        switch (type) {
            case "monthly" -> this.strategy = new MonthlyReportStrategy();
            case "yearly"  -> this.strategy = new YearlyReportStrategy();
            default        -> this.strategy = new DailyReportStrategy();
        }
    }
    /**
     * Thực thi strategy đang được chọn
     */
    public ReportDataDto execute(ShopOrderRepository orderRepo, OrderItemRepository itemRepo) {
        return strategy.execute(orderRepo, itemRepo);
    }
    public String getTypeName() {
        return strategy.getTypeName();
    }
}
