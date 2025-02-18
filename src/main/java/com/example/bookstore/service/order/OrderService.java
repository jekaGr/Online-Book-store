package com.example.bookstore.service.order;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDtoForUpdate;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(CreateOrderRequestDto requestDto, User user);

    List<OrderResponseDto> getOrders(User user, Pageable pageable);

    void changeOrderStatus(Long orderId,OrderDtoForUpdate dtoForUpdate, User user);

    List<OrderItemResponseDto> getOrderItems(Long orderId, User user, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId, User user);
}
