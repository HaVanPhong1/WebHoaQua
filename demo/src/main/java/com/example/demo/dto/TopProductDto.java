package com.example.demo.dto;
import java.math.BigDecimal;
/**
 * DTO cho sản phẩm bán chạy nhất trong kỳ báo cáo.
 * Được tạo từ JPQL query trả về Object[].
 */
public class TopProductDto {
    private String productName;
    private Long totalSold;         // Tổng số lượng đã bán
    private BigDecimal totalRevenue; // Tổng doanh thu từ sản phẩm này
    public TopProductDto(String productName, Long totalSold, BigDecimal totalRevenue) {
        this.productName = productName;
        this.totalSold = totalSold;
        this.totalRevenue = totalRevenue;
    }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getTotalSold() { return totalSold; }
    public void setTotalSold(Long totalSold) { this.totalSold = totalSold; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}
