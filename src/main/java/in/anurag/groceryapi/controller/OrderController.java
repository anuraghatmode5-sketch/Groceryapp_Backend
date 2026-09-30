package in.anurag.groceryapi.controller;

import in.anurag.groceryapi.dto.OrderRequest;
import in.anurag.groceryapi.dto.OrderResponse;
import in.anurag.groceryapi.dto.RazorpayOrderResponse;
import in.anurag.groceryapi.dto.RazorpayPaymentRequest;
import in.anurag.groceryapi.model.Order;
import in.anurag.groceryapi.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/cod")
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request) {

        try {

            OrderResponse response = orderService.placeOrder(request);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(new OrderResponse(false, e.getMessage()));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(new OrderResponse(
                            false,
                            "Error placing order: " + e.getMessage()
                    ));
        }
    }

    @PostMapping("/razorpay")
    public ResponseEntity<?> createRazorpayOrder(
            @Valid @RequestBody OrderRequest request) {

        try {
            RazorpayOrderResponse response =
                    orderService.createRazorpayOrder(request);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error creating order: " + e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders() {

        try {

            return ResponseEntity.ok(orderService.getAllOrders());

        } catch (Exception e) {

            Map<String, Object> errorResponse = new HashMap<>();

            errorResponse.put("success", false);
            errorResponse.put(
                    "message",
                    "Error fetching orders: " + e.getMessage()
            );

            return ResponseEntity.badRequest()
                    .body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserOrders(@PathVariable String userId) {

        try {

            List<Order> orders = orderService.getUserOrders(userId);

            return ResponseEntity.ok(orders);

        } catch (IllegalArgumentException e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error fetching orders");

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyRazorpayPayment(
            @RequestBody RazorpayPaymentRequest request) {

        try {

            boolean verified =
                    orderService.verifyRazorpayPayment(request);

            if (verified) {

                return ResponseEntity.ok(
                        Map.of(
                                "success", true,
                                "message", "Payment verified successfully"
                        )
                );
            }

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", "Payment verification failed"
                            )
                    );

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", e.getMessage()
                            )
                    );

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", "Error verifying payment"
                            )
                    );
        }
    }


}