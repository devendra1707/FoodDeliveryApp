package com.food.delivery.service.ServiceImpl;

import com.food.delivery.entity.CartEntity;
import com.food.delivery.repository.CartRepository;
import com.food.delivery.request.CartRequest;
import com.food.delivery.response.CartResponse;
import com.food.delivery.service.CartService;
import com.food.delivery.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final UserService userService;

   public CartServiceImpl(CartRepository cartRepository, UserService userService){
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Override
    public CartResponse addToCart(CartRequest request) {

       String loggedInUserId = userService.findByUserId();

      Optional<CartEntity> cartEntity = cartRepository.findByUserId(loggedInUserId);
      CartEntity cart = cartEntity.orElseGet(() -> new CartEntity(loggedInUserId, new HashMap<>()));
      Map<String, Integer> cartItems = cart.getItems();
      cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(), 0)+1);
      cart.setItems(cartItems);
       cart = cartRepository.save(cart);
         return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId();

       CartEntity entity = cartRepository.findByUserId(loggedInUserId)
               .orElse(new CartEntity(null,loggedInUserId, new HashMap<>()));

       return convertToResponse(entity);
    }

    @Transactional
    @Override
    public void clearCart() {
        String loggedInUserId = userService.findByUserId();

        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();

        CartEntity cartEntity = cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(() -> new RuntimeException("Cart is not found"));
        Map<String, Integer> cartMap = cartEntity.getItems();
        if (cartMap.containsKey(request.getFoodId())) {
            Integer currentQuantity = cartMap.get(request.getFoodId());
                if (currentQuantity > 0) {
                    cartMap.put(request.getFoodId(), currentQuantity - 1);
                } else {
                    cartMap.remove(request.getFoodId());
                }
                cartEntity = cartRepository.save(cartEntity);
            }
            return convertToResponse(cartEntity);
        }

    private CartResponse convertToResponse(CartEntity cartEntity){
      return CartResponse.builder()
               .id(cartEntity.getId())
               .userId(cartEntity.getUserId())
               .items(cartEntity.getItems()).build();
    }
}
