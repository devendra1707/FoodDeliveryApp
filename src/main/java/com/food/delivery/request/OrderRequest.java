package com.food.delivery.request;

import com.food.delivery.entity.OrderItemEntity;
import com.food.delivery.response.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private List<OrderItemEntity> orderItemList;

    private String userAddress;

    private double amount;

    private String email;

    private String phoneNumber;

    private String orderStatus;

}
