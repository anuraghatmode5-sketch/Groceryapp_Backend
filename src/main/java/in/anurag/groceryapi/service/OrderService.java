package in.anurag.groceryapi.service;

import in.anurag.groceryapi.dto.OrderRequest;
import in.anurag.groceryapi.dto.OrderResponse;
import in.anurag.groceryapi.dto.RazorpayOrderResponse;
import in.anurag.groceryapi.dto.RazorpayPaymentRequest;
import in.anurag.groceryapi.model.Order;
import in.anurag.groceryapi.model.Product;
import in.anurag.groceryapi.model.User;
import in.anurag.groceryapi.repository.AddressRepository;
import in.anurag.groceryapi.repository.OrderRepository;
import in.anurag.groceryapi.repository.ProductRepository;
import in.anurag.groceryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final RazorpayService razorpayService;

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;


    public OrderResponse placeOrder(OrderRequest request) {

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validate address exists
        addressRepository.findById(request.getAddress())
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        // Calculate total amount
        double totalAmount = 0.0;

        for (Order.OrderItem item : request.getItems()) {

            Product product = productRepository.findById(item.getProduct())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Product not found: " + item.getProduct()
                            ));

            // Use offer price if available, otherwise use regular price
            double price = product.getOfferPrice() != null
                    ? product.getOfferPrice()
                    : product.getPrice();

            totalAmount += price * item.getQuantity();
        }

        // Create order
        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setAddress(request.getAddress());
        order.setItems(request.getItems());
        order.setAmount(totalAmount);
        order.setStatus("Order Placed");
        order.setPaymentType("COD");
        order.setIsPaid(false);

        // Save order
        orderRepository.save(order);

        return new OrderResponse(
                true,
                "Order placed successfully"
        );
    }


    public RazorpayOrderResponse createRazorpayOrder(OrderRequest request) throws Exception {

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validate address exists
        addressRepository.findById(request.getAddress())
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        // Calculate total amount
        double totalAmount = 0.0;

        for (Order.OrderItem item : request.getItems()) {

            Product product = productRepository.findById(item.getProduct())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product not found: " + item.getProduct()
                    ));

            // Use offer price if available, otherwise use regular price
            double price = product.getOfferPrice() != null
                    ? product.getOfferPrice()
                    : product.getPrice();

            totalAmount += price * item.getQuantity();
        }

        // Convert amount to paise (1 rupee = 100 paise)
        int amountInPaise = (int) Math.round(totalAmount * 100);

        // Create Razorpay order
        JSONObject razorpayOrder =
                razorpayService.createOrder(amountInPaise, "INR");

        String razorpayOrderId = razorpayOrder.getString("id");

        // DO NOT create MongoDB Order here
        // Order will be created only after payment verification

        return new RazorpayOrderResponse(
                true,
                (int) Math.round(totalAmount),
                "INR",
                razorpayKeyId,
                null,
                razorpayOrderId
        );
    }

    public java.util.List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getUserOrders(String userId) {

        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return orderRepository.findByUserId(userId);
    }


    public boolean verifyRazorpayPayment(RazorpayPaymentRequest request) throws Exception {

        boolean verified = razorpayService.verifyPayment(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature()
        );

        if (!verified) {
            return false;
        }

        // Validate user exists
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Validate address exists
        addressRepository.findById(request.getAddress())
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        // Calculate total amount
        double totalAmount = 0.0;

        for (Order.OrderItem item : request.getItems()) {

            Product product = productRepository.findById(item.getProduct())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Product not found: " + item.getProduct()
                            )
                    );

            double price = product.getOfferPrice() != null
                    ? product.getOfferPrice()
                    : product.getPrice();

            totalAmount += price * item.getQuantity();
        }

        // Create order ONLY after successful payment verification
        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setAddress(request.getAddress());
        order.setItems(request.getItems());
        order.setAmount(totalAmount);
        order.setStatus("Order Placed");
        order.setPaymentType("Razorpay");
        order.setIsPaid(true);

        orderRepository.save(order);

        return true;
    }
}