package in.anurag.groceryapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;

    private List<String> description;

    private Double price;

    private Double offerPrice;

    private List<String> image;

    private String category;

    private Boolean inStock;

    @JsonProperty("_id")
    private String id;
}