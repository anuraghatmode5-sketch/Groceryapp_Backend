package in.anurag.groceryapi.dto;

import in.anurag.groceryapi.model.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddressRequest {

    @NotNull(message = "Address is required")
    @Valid
    private Address address;
}