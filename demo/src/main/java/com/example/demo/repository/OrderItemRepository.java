package com.example.demo.repository;
import com.example.demo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    /**
     * Lấy top N sản phẩm bán chạy nhất trong khoảng thời gian.
     * Trả về Object[]{ productName, totalQuantity, totalRevenue }
     * Dùng bởi Strategy Pattern để hiển thị bảng top sản phẩm.
     */
    @Query("SELECT i.product.name, SUM(i.quantity), SUM(i.unitPrice * i.quantity) " +
           "FROM OrderItem i " +
           "WHERE i.order.createdAt BETWEEN :from AND :to " +
           "GROUP BY i.product.name " +
           "ORDER BY SUM(i.quantity) DESC")
    List<Object[]> findTopProductsBetween(@Param("from") LocalDateTime from,
                                           @Param("to") LocalDateTime to,
                                           org.springframework.data.domain.Pageable pageable);
    /**
     * Overload với limit int thay vì Pageable (dùng trong Strategy)
     */
    default List<Object[]> findTopProductsBetween(LocalDateTime from, LocalDateTime to, int limit) {
        return findTopProductsBetween(from, to,
                org.springframework.data.domain.PageRequest.of(0, limit));
    }
}