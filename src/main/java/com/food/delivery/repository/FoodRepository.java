package com.food.delivery.repository;

import com.food.delivery.entity.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FoodRepository extends JpaRepository<FoodEntity, String> {

}
