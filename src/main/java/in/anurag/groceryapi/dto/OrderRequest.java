package in.anurag.groceryapi.dto;

import in.anurag.groceryapi.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "address is required")
    private String address;

    @NotNull(message = "items are required")
    private List<Order.OrderItem> items;
}