package com.example.bookstore.repository.order;

import com.example.bookstore.model.Order;
import com.example.bookstore.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUser(Long orderId, User user);

    List<Order> findAllByUserId(Long id, Pageable pageable);
}
