package in.anurag.groceryapi.service;

import in.anurag.groceryapi.dto.*;
import in.anurag.groceryapi.exception.ProductNotFoundException;
import in.anurag.groceryapi.model.Product;
import in.anurag.groceryapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;


    public ProductResponse addProduct(
            ProductRequest request,
            List<MultipartFile> images
    ) {

        // Upload images to Cloudinary
        List<String> imageUrls = cloudinaryService.uploadImages(images);

        // Create product entity
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOfferPrice(request.getOfferPrice());
        product.setCategory(request.getCategory());
        product.setImages(imageUrls);
        product.setInStock(true);

        // Save product
        productRepository.save(product);

        return new ProductResponse(true, "Product Added");
    }

    public ProductListResponse getAllProducts() {

        List<Product> products = productRepository.findAll();

        List<ProductDto> productDtos = products.stream()
                .map(product -> {
                    ProductDto dto = new ProductDto();

                    dto.setId(product.getId());
                    dto.setName(product.getName());
                    dto.setDescription(product.getDescription());
                    dto.setPrice(product.getPrice());
                    dto.setOfferPrice(product.getOfferPrice());
                    dto.setImage(product.getImages());
                    dto.setCategory(product.getCategory());
                    dto.setInStock(product.getInStock());

                    return dto;
                })
                .collect(Collectors.toList());

        return new ProductListResponse(productDtos, true);
    }

    public ProductDetailResponse getProductById(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id
                        ));

        ProductDto productDto = new ProductDto();

        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setOfferPrice(product.getOfferPrice());
        productDto.setImage(product.getImages());
        productDto.setCategory(product.getCategory());
        productDto.setInStock(product.getInStock());

        return new ProductDetailResponse(productDto, true);
    }

    public ProductResponse updateProductStock(String id, Boolean inStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Product not found with id: " + id
                ));

        product.setInStock(inStock);
        productRepository.save(product);

        return new ProductResponse(true, "Stock status updated");
    }
}
