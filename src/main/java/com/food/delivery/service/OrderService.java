package com.food.delivery.service;

import com.food.delivery.request.OrderRequest;
import com.food.delivery.response.OrderResponse;
import com.razorpay.RazorpayException;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;

public interface OrderService {

    OrderResponse createOrderWithPayment(OrderRequest orderRequest) throws RazorpayException;

    void verifyPayment(Map<String, String> paymentData, String status);

    List<OrderResponse> getUserOrders();

    void removeOrder(String orderId);

    List<OrderResponse> getOrdersOfAllUsers();

    void updateOrderStatus(String orderId, String status);
}
