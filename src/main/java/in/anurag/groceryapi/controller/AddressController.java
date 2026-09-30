package in.anurag.groceryapi.controller;

import in.anurag.groceryapi.dto.AddressListResponse;
import in.anurag.groceryapi.dto.AddressRequest;
import in.anurag.groceryapi.dto.AddressResponse;
import in.anurag.groceryapi.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/add")
    public ResponseEntity<AddressResponse> addAddress(
            @Valid @RequestBody AddressRequest request,
            Authentication authentication) {

        try {
            String userId = authentication.getName();

            AddressResponse response =
                    addressService.addAddress(userId, request.getAddress());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AddressResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<AddressListResponse> getUserAddresses(
            Authentication authentication) {

        try {

            // Get userId from authentication
            String userId = authentication.getName();

            AddressListResponse response =
                    addressService.getUserAddresses(userId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(new AddressListResponse(false, null));
        }
    }
}