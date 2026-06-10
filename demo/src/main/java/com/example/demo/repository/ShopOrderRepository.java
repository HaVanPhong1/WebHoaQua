package com.example.demo.repository;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.ShopOrder;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    List<ShopOrder> findByStatus(OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM ShopOrder o WHERE o.createdAt BETWEEN :from AND :to")
    BigDecimal totalRevenueBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COUNT(o) FROM ShopOrder o WHERE o.createdAt BETWEEN :from AND :to")
    Long countOrdersBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COUNT(o) FROM ShopOrder o WHERE o.status = :status AND o.createdAt BETWEEN :from AND :to")
    Long countByStatusBetween(@Param("status") OrderStatus status,
                               @Param("from") LocalDateTime from,
                               @Param("to") LocalDateTime to);
    @Query("SELECT i.product.category.name, COALESCE(SUM(i.unitPrice * i.quantity), 0) " +
           "FROM ShopOrder o JOIN o.items i " +
           "WHERE o.createdAt BETWEEN :from AND :to " +
           "GROUP BY i.product.category.name " +
           "ORDER BY SUM(i.unitPrice * i.quantity) DESC")
    List<Object[]> revenueByCategory(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}