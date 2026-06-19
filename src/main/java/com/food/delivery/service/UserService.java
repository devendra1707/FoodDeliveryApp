package com.food.delivery.service;

import com.food.delivery.security.UserRequest;
import com.food.delivery.security.UserResponse;

public interface UserService {

     UserResponse registerUser(UserRequest request);

     String findByUserId();
}
