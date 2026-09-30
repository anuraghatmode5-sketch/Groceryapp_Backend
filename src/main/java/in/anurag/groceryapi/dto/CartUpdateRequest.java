package in.anurag.groceryapi.dto;

import in.anurag.groceryapi.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class CartUpdateRequest {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "cartItems is required")
    private Map<String, User.CartItem> cartItems;
}