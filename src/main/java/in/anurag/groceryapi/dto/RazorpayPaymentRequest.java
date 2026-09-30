package in.anurag.groceryapi.dto;

import in.anurag.groceryapi.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class RazorpayPaymentRequest {

    private String userId;

    private String address;

    private List<Order.OrderItem> items;

    private String razorpayPaymentId;

    private String razorpayOrderId;

    private String razorpaySignature;
}