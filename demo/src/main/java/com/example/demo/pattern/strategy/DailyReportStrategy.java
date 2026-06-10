package com.example.demo.pattern.strategy;
import com.example.demo.dto.ReportDataDto;
import com.example.demo.dto.TopProductDto;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ShopOrderRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * STRATEGY PATTERN – Concrete Strategy: Thống kê theo NGÀY
 *
 * Chiến lược này tính doanh thu và số đơn cho 7 ngày gần nhất.
 * Trả về labels = ["10/06", "11/06", ...] và values tương ứng.
 */
public class DailyReportStrategy implements ReportStrategy {
    private static final DateTimeFormatter LABEL_FMT = DateTimeFormatter.ofPattern("dd/MM");
    @Override
    public ReportDataDto execute(ShopOrderRepository orderRepo, OrderItemRepository itemRepo) {
        ReportDataDto dto = new ReportDataDto();
        dto.setReportType("daily");
        List<String> labels = new ArrayList<>();
        List<BigDecimal> revenues = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        // Lấy dữ liệu 7 ngày gần nhất (ngày cũ → ngày mới)
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            LocalDateTime from = day.atStartOfDay();
            LocalDateTime to = day.plusDays(1).atStartOfDay();
            BigDecimal rev = orderRepo.totalRevenueBetween(from, to);
            Long cnt = orderRepo.countOrdersBetween(from, to);
            labels.add(day.format(LABEL_FMT));
            revenues.add(rev != null ? rev : BigDecimal.ZERO);
            counts.add(cnt != null ? cnt : 0L);
        }
        dto.setRevenueLabels(labels);
        dto.setRevenueValues(revenues);
        dto.setOrderCountValues(counts);
        // Tổng hợp 30 ngày gần nhất
        LocalDateTime from30 = LocalDateTime.now().minusDays(30);
        LocalDateTime now = LocalDateTime.now();
        BigDecimal totalRev = orderRepo.totalRevenueBetween(from30, now);
        dto.setTotalRevenue(totalRev != null ? totalRev : BigDecimal.ZERO);
        dto.setTotalOrders(orderRepo.countOrdersBetween(from30, now));
        dto.setNewOrders(orderRepo.countByStatusBetween(OrderStatus.NEW, from30, now));
        dto.setCancelledOrders(orderRepo.countByStatusBetween(OrderStatus.CANCELLED, from30, now));
        dto.setDeliveredOrders(orderRepo.countByStatusBetween(OrderStatus.DELIVERED, from30, now));
        // Top sản phẩm
        dto.setTopProducts(buildTopProducts(itemRepo, from30, now));
        // Doanh thu theo danh mục
        buildCategoryRevenue(dto, orderRepo, from30, now);
        return dto;
    }
    @Override
    public String getTypeName() {
        return "Theo ngày (7 ngày gần nhất)";
    }
    private List<TopProductDto> buildTopProducts(OrderItemRepository itemRepo,
                                                  LocalDateTime from, LocalDateTime to) {
        List<Object[]> rows = itemRepo.findTopProductsBetween(from, to, 5);
        List<TopProductDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            String name = (String) row[0];
            Long sold = ((Number) row[1]).longValue();
            BigDecimal rev = (BigDecimal) row[2];
            result.add(new TopProductDto(name, sold, rev));
        }
        return result;
    }
    private void buildCategoryRevenue(ReportDataDto dto, ShopOrderRepository orderRepo,
                                       LocalDateTime from, LocalDateTime to) {
        List<Object[]> rows = orderRepo.revenueByCategory(from, to);
        List<String> catLabels = new ArrayList<>();
        List<BigDecimal> catRevs = new ArrayList<>();
        for (Object[] row : rows) {
            catLabels.add((String) row[0]);
            catRevs.add((BigDecimal) row[1]);
        }
        dto.setCategoryLabels(catLabels);
        dto.setCategoryRevenues(catRevs);
    }
}
