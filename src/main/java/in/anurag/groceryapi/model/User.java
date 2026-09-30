package in.anurag.groceryapi.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private Map<String, CartItem> cartItems;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Data
    public static class CartItem {
        private Integer quantity;
    }
}
