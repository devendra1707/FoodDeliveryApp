package com.food.delivery.repository;

import com.food.delivery.security.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
   Optional<UserEntity> findByEmail(String email);
}
