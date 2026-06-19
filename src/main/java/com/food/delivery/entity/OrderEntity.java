package com.food.delivery.entity;

import com.food.delivery.response.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "order_entity")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  String id;

    private  String userId;

    private  String userAddress;

    private String phoneNumber;

    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItemEntity> orderItems;

    private Double amount;

    private  String paymentStatus;

    private String razorpayOrderId;

    private String razorpaySignature;

    String razorpayPaymentId;

    private String orderStatus;

}
