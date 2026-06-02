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
}
