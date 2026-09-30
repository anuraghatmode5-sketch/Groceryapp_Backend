package in.anurag.groceryapi.service;

import in.anurag.groceryapi.dto.CartUpdateRequest;
import in.anurag.groceryapi.dto.CartUpdateResponse;
import in.anurag.groceryapi.model.User;
import in.anurag.groceryapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;

    public CartUpdateResponse updateCart(CartUpdateRequest request) {

        // Find user by ID
        Optional<User> userOptional = userRepository.findById(request.getUserId());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        // Update the cart items
        if (user.getCartItems() == null) {
            user.setCartItems(new HashMap<>());
        }

        user.getCartItems().putAll(request.getCartItems());

        // Save the updated user
        userRepository.save(user);

        return new CartUpdateResponse(true, "Cart Updated");
    }



    public CartUpdateResponse removeCartItem(String userId, String productId) {

        // Find user by ID
        Optional<User> userOptional =
                userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        // Check cart exists
        if (user.getCartItems() == null) {
            throw new IllegalArgumentException("Cart is empty");
        }

        // Remove specific product
        user.getCartItems().remove(productId);

        // Save updated user
        userRepository.save(user);

        return new CartUpdateResponse(
                true,
                "Item removed from cart"
        );
    }
}
