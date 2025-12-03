package edu.poly.datn_sd52_fa25_huynq203.library.service.variant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.UploadFileDTO;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ImageResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ImageUploadResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.Image;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.variant.ImageRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.variant.ProductVariantRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.service.CloudinaryService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class VariantImageService {

    CloudinaryService cloudinaryService;
    ImageRepository imageRepository;
    ProductVariantRepository productVariantRepository;
    SseService sseService; // 1. INJECT SSE SERVICE
    ObjectMapper objectMapper; // INJECT OBJECT MAPPER

    // Sử dụng ExecutorService để chạy tác vụ upload Cloudinary (bất đồng bộ)
    ExecutorService executor = Executors.newCachedThreadPool();

    // Kênh SSE cố định để gửi trạng thái upload
    private static final String UPLOAD_TOPIC = "VARIANT_IMAGE_UPLOAD_STATUS";

    /**
     * Phương thức chính xử lý việc tải lên ảnh theo lô và gửi cập nhật SSE TỚI KÊNH.
     * Chạy trên luồng riêng (executor) để không block luồng request chính của Controller.
     */
    @Transactional
    public void processBatchImageUpload(Map<Long, List<UploadFileDTO>> variantFilesMap) {
        // Gửi thông báo bắt đầu (JSON String)
        sseService.sendToTopic(UPLOAD_TOPIC, "{\"status\": \"START\", \"message\": \"Bắt đầu tải lên ảnh.\" }");
        // Thực thi trong luồng riêng để trả về HTTP 200 ngay lập tức,
        // cho phép SSE duy trì kết nối và gửi data sau đó.
        executor.execute(() -> {
            try {
                for (Map.Entry<Long, List<UploadFileDTO>> entry : variantFilesMap.entrySet()) {
                    Long variantId = entry.getKey();
                    List<UploadFileDTO> files = entry.getValue();

                    if (files == null || files.isEmpty()) {
                        log.warn("Variant ID {} không có file nào để upload.", variantId);
                        continue;
                    }

                    // 1. Tìm ProductVariant
                    ProductVariant variant = productVariantRepository.findById(variantId)
                            .orElseThrow(() -> new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "ProductVariant ID không tồn tại: " + variantId));

                    // 💡 SỬA 3: Thay đổi kiểu file bên trong vòng lặp
                    for (UploadFileDTO fileDto : files) {
                        // 2. Thực hiện Upload lên Cloudinary và Lưu DB
                        // Lời gọi này đã đúng vì uploadAndSaveImage đã nhận UploadFileDTO
                        ImageResponse cloudinaryResponse = uploadAndSaveImage(variant, fileDto);

                        // 3. Server Push qua SSE TỚI KÊNH
                        sendUpdateToTopic(cloudinaryResponse);
                    }
                }
                log.info("Batch Image Upload hoàn tất và SSE Emitter đã đóng.");
            } catch (BusinessException e) {
                log.error("Lỗi nghiệp vụ trong quá trình xử lý Batch Image Upload: {}", e.getMessage());
                // Gửi thông báo lỗi nghiệp vụ qua kênh
                sseService.sendToTopic(UPLOAD_TOPIC, String.format("{\"status\": \"ERROR\", \"message\": \"Lỗi nghiệp vụ: %s\"}", e.getMessage()));

            } catch (Exception e) {
                log.error("Lỗi hệ thống trong quá trình xử lý Batch Image Upload: {}", e.getMessage(), e);
                // Gửi thông báo lỗi hệ thống qua kênh
                sseService.sendToTopic(UPLOAD_TOPIC, String.format("{\"status\": \"ERROR\", \"message\": \"Lỗi hệ thống: %s\"}", e.getMessage()));
            }
            // Lưu ý: Không gọi emitter.complete() vì kết nối được quản lý bởi SseService.
        });
    }

    /**
     * Upload 1 file lên Cloudinary và lưu thông tin vào DB.
     */
    public ImageResponse uploadAndSaveImage(ProductVariant variant, UploadFileDTO fileDto) {
        ImageUploadResponse uploadResult = cloudinaryService.uploadImage(fileDto.content(), fileDto.originalFilename(), null);

        // 1. Chuẩn bị Entity Image
        Image newImage = Image.builder()
                .variant(variant)
                .publicId(uploadResult.getPublicId())
                .cdnUrl(uploadResult.getCdnUrl())
                .build();

        // 2. Lưu vào CSDL
        Image saved = imageRepository.save(newImage);
        log.info("Lưu Image thành công vào CSDL, Public ID: {}", saved.getPublicId());

        // 3. Chuẩn bị Response để gửi qua SSE
        return ImageResponse.builder()
                .id(saved.getId())
                .variantId(saved.getVariant().getId())
                .publicId(saved.getPublicId())
                .cdnUrl(saved.getCdnUrl())
                .build();
    }

    /**
     * Gửi event SSE tới KÊNH đã đăng ký.
     */
    private void sendUpdateToTopic(ImageResponse imageResponse) throws JsonProcessingException {
        // Chuyển đối tượng Response thành JSON String
        String jsonPayload = objectMapper.writeValueAsString(imageResponse);

        // Gửi thông báo thành công cho 1 ảnh cụ thể qua kênh UPLOAD_TOPIC
        sseService.sendToTopic(UPLOAD_TOPIC, jsonPayload);

        log.info("Đã gửi SSE event cho Public ID: {} qua kênh {}", imageResponse.getPublicId(), UPLOAD_TOPIC);
    }

}
