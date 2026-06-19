package com.food.delivery.service;

import com.food.delivery.request.CartRequest;
import com.food.delivery.response.CartResponse;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    CartResponse getCart();

    void clearCart();

   CartResponse removeFromCart(CartRequest request);
}
