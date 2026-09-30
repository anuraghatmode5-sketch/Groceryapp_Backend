package in.anurag.groceryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AddressListResponse {

    private Boolean success;
    private List<AddressDetailsDto> addresses;
}