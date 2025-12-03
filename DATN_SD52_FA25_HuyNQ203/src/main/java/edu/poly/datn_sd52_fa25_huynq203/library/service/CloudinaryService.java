package edu.poly.datn_sd52_fa25_huynq203.library.service;

import com.cloudinary.Cloudinary;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CloudinaryService {

    Cloudinary cloudinary;

    @Transactional(readOnly = true)
    public ImageUploadResponse uploadImage(byte[] fileBytes, String originalFilename, String publicIdToOverride) {
        // Kiểm tra byte[] và tên file
        if (fileBytes == null || fileBytes.length == 0 || originalFilename == null || originalFilename.isEmpty())
            throw new IllegalArgumentException("File upload không hợp lệ hoặc rỗng.");

        Map<String, Object> params = new HashMap<>();
        params.put("folder", "datn_sd52_fa25_huynq203/product_variants");
        params.put("resource_type", "image");

        // Logic Ghi đè (Sử dụng if đơn giản để thêm hai tham số có điều kiện)
        if (publicIdToOverride != null && !publicIdToOverride.isEmpty()) {
            params.put("public_id", publicIdToOverride);
            params.put("overwrite", true);
            log.info("Thực hiện Ghi đè lên Public ID: {}", publicIdToOverride);
        } else {
            log.info("Thực hiện Tải lên mới vào thư mục: {}", "datn_sd52_fa25_huynq203/product_variants");
        }
        try {
            // 3. Thực hiện Upload, truyền đúng Map tham số đã thiết lập
            Map<?, ?> uploadResult = cloudinary.uploader().upload(fileBytes, params);
            log.info("Cloudinary Upload Success: {}", uploadResult.get("public_id"));

            // 4. Trả về Response, ưu tiên secure_url cho sản phẩm E-Commerce
            return ImageUploadResponse.builder()
                    .publicId(uploadResult.get("public_id").toString())
                    .cdnUrl(uploadResult.get("secure_url").toString())
                    .build();
        } catch (Exception e) {
            log.error("Cloudinary Upload Failed: {}", e.getMessage());
            throw new BusinessException(ExceptionType.CloudinaryOperationException);
        }
    }
}
