package in.anurag.groceryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.anurag.groceryapi.dto.*;
import in.anurag.groceryapi.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping("/list")
    public ResponseEntity<ProductListResponse> getAllProducts() {

        ProductListResponse response = productService.getAllProducts();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/id")
    public ResponseEntity<ProductDetailResponse> getProductById(
            @RequestParam("id") String id) {

        ProductDetailResponse response = productService.getProductById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/id")
    public ResponseEntity<ProductDetailResponse> getProductByIdPost(
            @Valid @RequestBody ProductIdRequest request) {

        ProductDetailResponse response =
                productService.getProductById(request.getId());

        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> addProduct(
            @RequestParam("productData") String productDataJson,
            @RequestParam("images") List<MultipartFile> images
    ) {

        try {
            // Parse JSON string to ProductRequest
            ProductRequest productRequest =
                    objectMapper.readValue(productDataJson, ProductRequest.class);

            // Validate images
            if (images == null || images.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ProductResponse(false, "At least one image is required"));
            }

            ProductResponse response =
                    productService.addProduct(productRequest, images);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(new ProductResponse(
                            false,
                            "Failed to add product: " + e.getMessage()
                    ));
        }
    }

    @PostMapping("/stock")
    public ResponseEntity<ProductResponse> updateProductStock(
            @Valid @RequestBody ProductStockRequest request) {

        try {
            ProductResponse response = productService.updateProductStock(
                    request.getId(),
                    request.getInStock()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ProductResponse(false, e.getMessage()));
        }
    }


}