package in.anurag.groceryapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import in.anurag.groceryapi.dto.CartUpdateRequest;
import in.anurag.groceryapi.dto.CartUpdateResponse;
import in.anurag.groceryapi.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/update")
    public ResponseEntity<CartUpdateResponse> updateCart(
            @Valid @RequestBody CartUpdateRequest request) {

        try {
            CartUpdateResponse response = cartService.updateCart(request);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new CartUpdateResponse(false, e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new CartUpdateResponse(false, "Error updating cart"));
        }
    }


    @DeleteMapping("/remove")
    public ResponseEntity<CartUpdateResponse> removeCartItem(
            @RequestParam String userId,
            @RequestParam String productId) {

        try {

            CartUpdateResponse response =
                    cartService.removeCartItem(
                            userId,
                            productId
                    );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(
                            new CartUpdateResponse(
                                    false,
                                    e.getMessage()
                            )
                    );

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(
                            new CartUpdateResponse(
                                    false,
                                    "Error removing item from cart"
                            )
                    );
        }
    }
}