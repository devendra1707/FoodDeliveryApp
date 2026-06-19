package com.food.delivery.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.delivery.request.FoodRequest;
import com.food.delivery.response.FoodResponse;
import com.food.delivery.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/food/")
@AllArgsConstructor
@CrossOrigin("*")
public class FoodController {

    private final FoodService foodService;

    @PostMapping("_add")
    public FoodResponse addFood(
            @RequestPart("food") String foodString,
            @RequestPart("file") MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request;

        try {
            request = objectMapper.readValue(foodString, FoodRequest.class);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid JSON format..."
            );
        }

        return foodService.addFood(request, file);
    }

    @PostMapping("_foods")
    public ResponseEntity<List<FoodResponse>> readFood(){
       List<FoodResponse> getAllFood = foodService.readFoods();
       return new ResponseEntity<>(getAllFood,HttpStatus.OK);
    }

    @PostMapping("{id}")
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable("id") String id){
        FoodResponse getFood = foodService.readFood(id);
        return new ResponseEntity<>(getFood,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(@PathVariable("id") String id){
    foodService.deleteFood(id);
    }
}