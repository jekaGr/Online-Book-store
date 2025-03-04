package com.example.bookstore.repository.orderitem;

import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderId(Long orderItemId, Long id);

    List<OrderItem> findAllByOrder(Order order, Pageable pageable);
}
