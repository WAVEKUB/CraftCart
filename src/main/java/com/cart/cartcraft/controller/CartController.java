package com.cart.cartcraft.controller;

import com.cart.cartcraft.exception.ResourceNotFoundException;
import com.cart.cartcraft.model.Cart;
import com.cart.cartcraft.model.User;
import com.cart.cartcraft.response.ApiResponse;
import com.cart.cartcraft.service.cart.ICartService;
import com.cart.cartcraft.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;
    private final IUserService userService;

    @GetMapping("/my-cart")
    public ResponseEntity<ApiResponse> getCart() {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse> clearCart() {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
            }
            cartService.clearCart(cart.getId());
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount() {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            if (cart == null) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Cart not found", null));
            }
            BigDecimal totalPrice = cartService.getTotalPrice(cart.getId());
            return ResponseEntity.ok(new ApiResponse("Total Price", totalPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
