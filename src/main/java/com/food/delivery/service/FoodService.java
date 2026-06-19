package com.food.delivery.service;

import com.food.delivery.request.FoodRequest;
import com.food.delivery.response.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    FoodResponse addFood(FoodRequest request, MultipartFile file);
    List<FoodResponse> readFoods();
    FoodResponse readFood(String id);
    void deleteFood(String id);
}
