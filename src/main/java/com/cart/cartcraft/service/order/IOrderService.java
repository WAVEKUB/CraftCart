package com.cart.cartcraft.service.order;

import com.cart.cartcraft.dto.OrderDto;
import com.cart.cartcraft.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrder(Long userId);

    OrderDto convertToDto(Order order);
}