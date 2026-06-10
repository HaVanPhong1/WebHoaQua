package com.example.demo.pattern.strategy;
import com.example.demo.dto.ReportDataDto;
import com.example.demo.dto.TopProductDto;
import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.ShopOrderRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/**
 * STRATEGY PATTERN – Concrete Strategy: Thống kê theo NĂM
 *
 * Chiến lược này tính doanh thu và số đơn cho 12 tháng của năm hiện tại.
 * Labels = ["Tháng 1", "Tháng 2", ..., "Tháng 12"].
 */
public class YearlyReportStrategy implements ReportStrategy {
    @Override
    public ReportDataDto execute(ShopOrderRepository orderRepo, OrderItemRepository itemRepo) {
        ReportDataDto dto = new ReportDataDto();
        dto.setReportType("yearly");
        List<String> labels = new ArrayList<>();
        List<BigDecimal> revenues = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        // 12 tháng của năm hiện tại
        for (int m = 1; m <= 12; m++) {
            LocalDateTime from = LocalDateTime.of(currentYear, m, 1, 0, 0);
            LocalDateTime to = from.plusMonths(1);
            BigDecimal rev = orderRepo.totalRevenueBetween(from, to);
            Long cnt = orderRepo.countOrdersBetween(from, to);
            labels.add("Tháng " + m);
            revenues.add(rev != null ? rev : BigDecimal.ZERO);
            counts.add(cnt != null ? cnt : 0L);
        }
        dto.setRevenueLabels(labels);
        dto.setRevenueValues(revenues);
        dto.setOrderCountValues(counts);
        // Tổng hợp cả năm
        LocalDateTime fromYear = LocalDateTime.of(currentYear, 1, 1, 0, 0);
        LocalDateTime toYear = LocalDateTime.of(currentYear + 1, 1, 1, 0, 0);
        BigDecimal totalRev = orderRepo.totalRevenueBetween(fromYear, toYear);
        dto.setTotalRevenue(totalRev != null ? totalRev : BigDecimal.ZERO);
        dto.setTotalOrders(orderRepo.countOrdersBetween(fromYear, toYear));
        dto.setNewOrders(orderRepo.countByStatusBetween(OrderStatus.NEW, fromYear, toYear));
        dto.setCancelledOrders(orderRepo.countByStatusBetween(OrderStatus.CANCELLED, fromYear, toYear));
        dto.setDeliveredOrders(orderRepo.countByStatusBetween(OrderStatus.DELIVERED, fromYear, toYear));
        // Top sản phẩm cả năm
        List<Object[]> rows = itemRepo.findTopProductsBetween(fromYear, toYear, 5);
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
        List<Object[]> catRows = orderRepo.revenueByCategory(fromYear, toYear);
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
        return "Theo năm (" + LocalDate.now().getYear() + ")";
    }
}
