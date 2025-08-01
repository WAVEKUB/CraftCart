package com.cart.cartcraft.request;

import com.cart.cartcraft.model.Category;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int quantity;
    private String description;

    private Category category;
}
