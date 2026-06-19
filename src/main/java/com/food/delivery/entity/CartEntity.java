package com.food.delivery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_entity")
public class CartEntity {

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private String id;
    private String userId;

    @ElementCollection
    @CollectionTable(
            name = "cart_items",
            joinColumns = @JoinColumn(name = "cart_id")
    )
    @MapKeyColumn(name = "food_id")
    @Column(name = "quantity")
    private Map<String , Integer> items = new HashMap<>();

    public CartEntity(String userId, Map<String, Integer> items){
        this.userId = userId;
        this.items = items;
    }
}
