package in.anurag.groceryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartUpdateResponse {

    private Boolean success;
    private String message;
}