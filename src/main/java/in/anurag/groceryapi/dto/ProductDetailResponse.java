package in.anurag.groceryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {

    private ProductDto product;
    private Boolean success;
}