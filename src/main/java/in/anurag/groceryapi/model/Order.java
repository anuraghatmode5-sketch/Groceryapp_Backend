package in.anurag.groceryapi.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
@Data
public class Order {

    @Id
    private String id;

    private String userId;

    private List<OrderItem> items;

    private Double amount;

    private String address;

    private String status = "Order Placed";

    private String paymentType;

    private Boolean isPaid = false;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Data
    public static class OrderItem {

        private String product;

        private Integer quantity;
    }
}