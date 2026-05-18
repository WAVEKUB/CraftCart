package com.cart.cartcraft.dto;

import com.cart.cartcraft.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal orderTotalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemDto> items;
}