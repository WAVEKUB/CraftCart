package com.cart.cartcraft.dto;

import com.cart.cartcraft.model.Category;
import com.cart.cartcraft.model.Image;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private Double price;
    private Integer quantity;
    private String description;
    private Category category;
    private List<ImageDto> images;
}
