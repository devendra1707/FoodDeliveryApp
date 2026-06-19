package com.food.delivery.service.ServiceImpl;

import com.food.delivery.entity.OrderEntity;
import com.food.delivery.entity.OrderItemEntity;
import com.food.delivery.repository.CartRepository;
import com.food.delivery.repository.OrderRepository;
import com.food.delivery.request.OrderRequest;
import com.food.delivery.response.OrderItem;
import com.food.delivery.response.OrderResponse;
import com.food.delivery.service.OrderService;
import com.food.delivery.service.UserService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CartRepository cartRepository;

    @Value("${razorpay_key}")
    private String RAZORPAY_KEY;

    @Value("${razorpay_secret}")
    private String RAZORPAY_SECRET;

    @Override
    public OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException {

        // Convert request to entity
        OrderEntity newOrder = convertToEntity(request);

        // Set logged-in user
        String loggedInUserID = userService.findByUserId();
        newOrder.setUserId(loggedInUserID);

        // Create Razorpay client
        RazorpayClient razorpayClient = new RazorpayClient(RAZORPAY_KEY, RAZORPAY_SECRET);

        // Convert amount (₹) to paise
        long amountInPaise = Math.round(newOrder.getAmount() * 100);

        JSONObject razorpayRequest = new JSONObject();
        razorpayRequest.put("amount", amountInPaise);
        razorpayRequest.put("currency", "INR");
        razorpayRequest.put("payment_capture", 1);

        // Create Razorpay Order
        Order razorpayOrder = razorpayClient.orders.create(razorpayRequest);

        // Save Razorpay Order ID
        newOrder.setRazorpayOrderId(razorpayOrder.get("id"));

        // Optional defaults
        if (newOrder.getPaymentStatus() == null) {
            newOrder.setPaymentStatus("Pending");
        }

        if (newOrder.getOrderStatus() == null) {
            newOrder.setOrderStatus("Preparing");
        }

        // Save order in DB
        newOrder = orderRepository.save(newOrder);

        return convertToResponse(newOrder);
    }

    @Override
    @Transactional
    public void verifyPayment(Map<String, String> paymentData, String status) {

        String razorpayOrderId = paymentData.get("razorpay_order_id");

        OrderEntity existingOrder = orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        existingOrder.setPaymentStatus(status);
        existingOrder.setRazorpaySignature(paymentData.get("razorpay_signature"));
        existingOrder.setRazorpayPaymentId(paymentData.get("razorpay_payment_id"));

        if ("Paid".equalsIgnoreCase(status)) {
            existingOrder.setOrderStatus("Confirmed");
        }

        orderRepository.save(existingOrder);

        if ("Paid".equalsIgnoreCase(status)) {
            cartRepository.deleteByUserId(existingOrder.getUserId());
        }
    }

    @Override
    public List<OrderResponse> getUserOrders() {
        String loggedInUserId = userService.findByUserId();
        List<OrderEntity> list = orderRepository.findByUserId(loggedInUserId);
        return list.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public void removeOrder(String orderId) {
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderResponse> getOrdersOfAllUsers() {

       List<OrderEntity> list = orderRepository.findAll();
       return list.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(String orderId, String status) {
        OrderEntity entity = orderRepository.findById(orderId)
                 .orElseThrow(() -> new RuntimeException("Order not Found"));
        entity.setOrderStatus(status);
        orderRepository.save(entity);
    }

    private OrderResponse convertToResponse(OrderEntity newOrder) {
        return OrderResponse.builder()
                .id(newOrder.getId())
                .amount(newOrder.getAmount())
                .userAddress(newOrder.getUserAddress())
                .userId(newOrder.getUserId())
                .razorpayOrderId(newOrder.getRazorpayOrderId())
                .paymentStatus(newOrder.getPaymentStatus())
                .orderStatus(newOrder.getOrderStatus())
                .email(newOrder.getEmail())
                .phoneNumber(newOrder.getPhoneNumber())
                .orderStatus(newOrder.getOrderStatus())
                .orderItems(convertOrderItems(newOrder.getOrderItems()))
                .build();
    }

    private List<OrderItem> convertOrderItems(List<OrderItemEntity> items) {

        if (items == null) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(item -> OrderItem.builder()
                        .foodId(item.getFoodId())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .category(item.getCategory())
                        .imageUrl(item.getImageUrl())
                        .description(item.getDescription())
                        .name(item.getName())
                        .build())
                .toList();
    }

    private OrderEntity convertToEntity(OrderRequest request) {
       return OrderEntity.builder()
        .userAddress(request.getUserAddress())
        .amount(request.getAmount())
        .orderItems(request.getOrderItemList())
               .email(request.getEmail())
               .phoneNumber(request.getPhoneNumber())
               .orderStatus(request.getOrderStatus())
        .build();
    }
}
