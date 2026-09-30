package in.anurag.groceryapi.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public List<String> uploadImages(List<MultipartFile> images) {

        List<String> imageUrls = new ArrayList<>();

        images.forEach(image -> {
            try {
                Map uploadResult = cloudinary.uploader().upload(
                        image.getBytes(),
                        ObjectUtils.emptyMap()
                );

                String imageUrl = (String) uploadResult.get("secure_url");

                imageUrls.add(imageUrl);

            } catch (IOException e) {
                throw new RuntimeException(
                        "Failed to upload image: " + e.getMessage()
                );
            }
        });

        return imageUrls;
    }
}