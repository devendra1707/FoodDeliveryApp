package com.food.delivery.repository;

import com.food.delivery.entity.CartEntity;
import com.food.delivery.response.CartResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, String> {

    Optional<CartEntity>  findByUserId(String userID);

    void deleteByUserId(String userId);

}
