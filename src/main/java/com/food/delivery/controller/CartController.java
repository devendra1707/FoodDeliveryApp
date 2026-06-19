package com.food.delivery.controller;

import com.food.delivery.request.CartRequest;
import com.food.delivery.response.CartResponse;
import com.food.delivery.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/food/cart/")
public class CartController {

    private final CartService cartService;

    public  CartController(CartService cartService){
        this.cartService = cartService;
    }

    @PostMapping(value = "_add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody CartRequest request){
       String foodId = request.getFoodId();
       if(foodId == null || foodId.isEmpty()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FoodId not formate");
       }
        CartResponse response = cartService.addToCart(request);
       return ResponseEntity.ok(response);
    }

    @GetMapping(value = "_get")
    public ResponseEntity<CartResponse> getCart(){
        CartResponse response = cartService.getCart();
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "_clearCart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(){
    cartService.clearCart();
    }

    @PostMapping(value = "_remove")
    public CartResponse removeFromCart(@RequestBody CartRequest cartRequest){
        String foodId = cartRequest.getFoodId();
        if(foodId == null || foodId.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FoodId not formate");
        }
       return cartService.removeFromCart(cartRequest);
    }
}
