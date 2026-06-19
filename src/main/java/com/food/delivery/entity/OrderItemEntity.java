package com.food.delivery.entity;

import com.food.delivery.response.OrderItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String foodId;

    private Integer quantity;

    private Double price;

    private String category;

    private String imageUrl;

    private String description;

    private String name;

}