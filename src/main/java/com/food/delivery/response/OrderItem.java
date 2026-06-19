package com.food.delivery.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {

    private String foodId;

    private Integer quantity;

    private Double price;

    private String category;

    private String imageUrl;

    private  String description;

    private String name;
}
