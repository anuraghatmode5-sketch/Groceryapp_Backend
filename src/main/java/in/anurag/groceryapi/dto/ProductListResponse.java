package in.anurag.groceryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductListResponse {

    private List<ProductDto> products;
    private Boolean success;
}