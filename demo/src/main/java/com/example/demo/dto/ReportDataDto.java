package com.example.demo.dto;
import java.math.BigDecimal;
import java.util.List;
/**
 * DTO chứa toàn bộ dữ liệu để hiển thị trang báo cáo.
 * Được Strategy tạo ra và truyền vào Model của Controller.
 */
public class ReportDataDto {
    // Dữ liệu cho biểu đồ doanh thu (trục X = nhãn thời gian, trục Y = doanh thu)
    private List<String> revenueLabels;
    private List<BigDecimal> revenueValues;
    // Dữ liệu cho biểu đồ số lượng đơn hàng
    private List<Long> orderCountValues;
    // Thống kê tổng hợp
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long newOrders;
    private Long cancelledOrders;
    private Long deliveredOrders;
    // Top sản phẩm bán chạy
    private List<TopProductDto> topProducts;
    // Doanh thu theo danh mục
    private List<String> categoryLabels;
    private List<BigDecimal> categoryRevenues;
    // Loại báo cáo hiện tại
    private String reportType;
    // Constructor rỗng
    public ReportDataDto() {}
    // ====== Getters & Setters ======
    public List<String> getRevenueLabels() { return revenueLabels; }
    public void setRevenueLabels(List<String> revenueLabels) { this.revenueLabels = revenueLabels; }
    public List<BigDecimal> getRevenueValues() { return revenueValues; }
    public void setRevenueValues(List<BigDecimal> revenueValues) { this.revenueValues = revenueValues; }
    public List<Long> getOrderCountValues() { return orderCountValues; }
    public void setOrderCountValues(List<Long> orderCountValues) { this.orderCountValues = orderCountValues; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public Long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }
    public Long getNewOrders() { return newOrders; }
    public void setNewOrders(Long newOrders) { this.newOrders = newOrders; }
    public Long getCancelledOrders() { return cancelledOrders; }
    public void setCancelledOrders(Long cancelledOrders) { this.cancelledOrders = cancelledOrders; }
    public Long getDeliveredOrders() { return deliveredOrders; }
    public void setDeliveredOrders(Long deliveredOrders) { this.deliveredOrders = deliveredOrders; }
    public List<TopProductDto> getTopProducts() { return topProducts; }
    public void setTopProducts(List<TopProductDto> topProducts) { this.topProducts = topProducts; }
    public List<String> getCategoryLabels() { return categoryLabels; }
    public void setCategoryLabels(List<String> categoryLabels) { this.categoryLabels = categoryLabels; }
    public List<BigDecimal> getCategoryRevenues() { return categoryRevenues; }
    public void setCategoryRevenues(List<BigDecimal> categoryRevenues) { this.categoryRevenues = categoryRevenues; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
}
