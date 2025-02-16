package com.example.bookstore.service.order;

import com.example.bookstore.dto.order.CreateOrderRequestDto;
import com.example.bookstore.dto.order.OrderDtoForUpdate;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.orderitem.OrderItemResponseDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.OrderProcessingException;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.order.OrderRepository;
import com.example.bookstore.repository.orderitem.OrderItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto orderRequestDto, User user) {
        ShoppingCart cart = getShoppingCartByUser(user);
        Set<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new OrderProcessingException(
                    "Add items before checkout."
            );
        }
        Order order = buildOrder(orderRequestDto.getShippingAddress(), user, cartItems);
        orderRepository.save(order);
        Set<OrderItem> orderItems = createOrderItemsSet(cartItems, order);
        order.setOrderItems(orderItems);
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteByShoppingCart(user.getId());
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getOrders(User user, Pageable pageable) {
        return orderRepository.findAllByUserId(user.getId(), pageable).stream()
                .map(orderMapper::toDto).toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId, User user) {
        Order order = getUserOrderById(orderId, user);
        return orderItemMapper.toDto(orderItemRepository
                .findByIdAndOrderId(orderItemId, order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find order item by id '"
                        + orderItemId
                        + "' for user with email '" + user.getEmail() + "'")));
    }

    @Override
    public void changeOrderStatus(Long orderId, OrderDtoForUpdate dtoForUpdate, User user) {
        Order order = getUserOrderById(orderId, user);
        order.setStatus(dtoForUpdate.getStatus());
        orderRepository.save(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long orderId, User user, Pageable pageable) {
        Order order = getUserOrderById(orderId, user);
        return orderItemRepository.findAllByOrder(order, pageable).stream()
                .map(orderItemMapper::toDto).toList();
    }

    private ShoppingCart getShoppingCartByUser(User user) {
        return shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart for user with email '"
                        + user.getEmail()
                        + "' is empty"));
    }

    private Order buildOrder(String shippingAddress, User user, Set<CartItem> cartItems) {
        return new Order()
                .setShippingAddress(shippingAddress)
                .setUser(user).setOrderDate(LocalDateTime.now())
                .setStatus(Order.Status.PENDING)
                .setTotal(getTotalBalanceForCartItems(cartItems));
    }

    private Set<OrderItem> createOrderItemsSet(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> new OrderItem()
                        .setBook(cartItem.getBook())
                        .setQuantity(cartItem.getQuantity())
                        .setOrder(order)
                        .setPrice(cartItem.getBook().getPrice()))
                .collect(Collectors.toSet());
    }

    private BigDecimal getTotalBalanceForCartItems(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order getUserOrderById(Long orderId, User user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id '"
                        + orderId
                        + "' for user with email '"
                        + user.getEmail() + "'"));
    }
}
