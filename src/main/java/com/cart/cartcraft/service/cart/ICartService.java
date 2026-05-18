package com.cart.cartcraft.service.cart;

import com.cart.cartcraft.model.Cart;
import com.cart.cartcraft.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
}
