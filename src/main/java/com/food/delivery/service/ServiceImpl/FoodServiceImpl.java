package com.food.delivery.service.ServiceImpl;

import com.food.delivery.entity.FoodEntity;
import com.food.delivery.repository.FoodRepository;
import com.food.delivery.request.FoodRequest;
import com.food.delivery.response.FoodResponse;
import com.food.delivery.service.FoodService;
import com.food.delivery.storage.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {

        FoodEntity newFoodEntity = convertToEntity(request);
        String imageUrl = fileStorageService.uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);

        newFoodEntity = foodRepository.save(newFoodEntity);
       return convertToResponse(newFoodEntity);
    }

    private FoodEntity convertToEntity(FoodRequest request){
      return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }

    private FoodResponse convertToResponse(FoodEntity foodEntity){
       return FoodResponse.builder()
                .id(foodEntity.getId())
                .name(foodEntity.getName())
                .description(foodEntity.getDescription())
                .price(foodEntity.getPrice())
                .category(foodEntity.getCategory())
                .imageUrl(foodEntity.getImageUrl())
                .build();
    }

    @Override
    public List<FoodResponse> readFoods() {
        List<FoodEntity> allFoodDetails = foodRepository.findAll();
       return allFoodDetails.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {

        FoodEntity getFoodById = foodRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Food not found with id: " + id));
        return convertToResponse(getFoodById);
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response = readFood(id);
        String imageUrl = response.getImageUrl();
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isFileDeleted = fileStorageService.deleteFile(fileName);
        if(isFileDeleted){
            foodRepository.deleteById(response.getId());
        }

    }


}