package com.food.delivery.controller;

import com.food.delivery.request.OrderRequest;
import com.food.delivery.response.OrderResponse;
import com.food.delivery.service.OrderService;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/_create")
    public ResponseEntity<?> createOrderWithPayment(@RequestBody OrderRequest request) {
        try {
            OrderResponse response = orderService.createOrderWithPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RazorpayException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    @PostMapping("/_verify")
    public void verifyPayment(@RequestBody Map<String, String> paymentData){
    orderService.verifyPayment(paymentData,"Paid");
    }

    @GetMapping("/_get")
    public List<OrderResponse> getOrders(){
    return orderService.getUserOrders();
    }

    @DeleteMapping("/delete/_{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String orderId){
    orderService.removeOrder(orderId);
    }

    @GetMapping("/_all")
    public List<OrderResponse> getOrdersOfAllUsers(){
        return orderService.getOrdersOfAllUsers();
    }

    @PatchMapping("/status/_{orderId}")
    public void updateOrderStatus(@PathVariable String orderId, @RequestParam String status){

        orderService.updateOrderStatus(orderId,status);
    }

}
