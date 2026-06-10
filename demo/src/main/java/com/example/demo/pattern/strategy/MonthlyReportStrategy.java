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
 * STRATEGY PATTERN – Concrete Strategy: Thống kê theo THÁNG
 *
 * Chiến lược này tính doanh thu và số đơn cho 6 tháng gần nhất.
 * Labels = ["01/2026", "02/2026", ...].
 */
public class MonthlyReportStrategy implements ReportStrategy {
    private static final DateTimeFormatter LABEL_FMT = DateTimeFormatter.ofPattern("MM/yyyy");
    @Override
    public ReportDataDto execute(ShopOrderRepository orderRepo, OrderItemRepository itemRepo) {
        ReportDataDto dto = new ReportDataDto();
        dto.setReportType("monthly");
        List<String> labels = new ArrayList<>();
        List<BigDecimal> revenues = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        // 6 tháng gần nhất
        LocalDate today = LocalDate.now();
        for (int i = 5; i >= 0; i--) {
            LocalDate month = today.minusMonths(i).withDayOfMonth(1);
            LocalDateTime from = month.atStartOfDay();
            LocalDateTime to = month.plusMonths(1).atStartOfDay();
            BigDecimal rev = orderRepo.totalRevenueBetween(from, to);
            Long cnt = orderRepo.countOrdersBetween(from, to);
            labels.add(month.format(LABEL_FMT));
            revenues.add(rev != null ? rev : BigDecimal.ZERO);
            counts.add(cnt != null ? cnt : 0L);
        }
        dto.setRevenueLabels(labels);
        dto.setRevenueValues(revenues);
        dto.setOrderCountValues(counts);
        // Tổng hợp 6 tháng
        LocalDateTime from6m = LocalDate.now().minusMonths(6).withDayOfMonth(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal totalRev = orderRepo.totalRevenueBetween(from6m, now);
        dto.setTotalRevenue(totalRev != null ? totalRev : BigDecimal.ZERO);
        dto.setTotalOrders(orderRepo.countOrdersBetween(from6m, now));
        dto.setNewOrders(orderRepo.countByStatusBetween(OrderStatus.NEW, from6m, now));
        dto.setCancelledOrders(orderRepo.countByStatusBetween(OrderStatus.CANCELLED, from6m, now));
        dto.setDeliveredOrders(orderRepo.countByStatusBetween(OrderStatus.DELIVERED, from6m, now));
        // Top sản phẩm
        List<Object[]> rows = itemRepo.findTopProductsBetween(from6m, now, 5);
        List<TopProductDto> topProducts = new ArrayList<>();
        for (Object[] row : rows) {
            topProducts.add(new TopProductDto(
                    (String) row[0],
                    ((Number) row[1]).longValue(),
                    (BigDecimal) row[2]
            ));
        }
        dto.setTopProducts(topProducts);
        // Danh mục
        List<Object[]> catRows = orderRepo.revenueByCategory(from6m, now);
        List<String> catLabels = new ArrayList<>();
        List<BigDecimal> catRevs = new ArrayList<>();
        for (Object[] row : catRows) {
            catLabels.add((String) row[0]);
            catRevs.add((BigDecimal) row[1]);
        }
        dto.setCategoryLabels(catLabels);
        dto.setCategoryRevenues(catRevs);
        return dto;
    }
    @Override
    public String getTypeName() {
        return "Theo tháng (6 tháng gần nhất)";
    }
}