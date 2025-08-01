package com.cart.cartcraft.service.user;

import com.cart.cartcraft.dto.UserDto;
import com.cart.cartcraft.model.User;
import com.cart.cartcraft.request.CreateUserRequest;
import com.cart.cartcraft.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticatedUser();
}