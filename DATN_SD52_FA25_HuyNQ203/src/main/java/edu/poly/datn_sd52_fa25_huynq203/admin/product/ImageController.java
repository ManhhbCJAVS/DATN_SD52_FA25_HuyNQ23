package edu.poly.datn_sd52_fa25_huynq203.admin.product;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.UploadFileDTO;
import edu.poly.datn_sd52_fa25_huynq203.library.service.variant.VariantImageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${backoffice.endpoint}/image")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Validated
@Slf4j
public class ImageController {

    VariantImageService variantImageService;

    /**
     * Endpoint nhận request upload ảnh và kích hoạt tác vụ bất đồng bộ.
     * Trả về 202 ACCEPTED để thông báo tác vụ đã được nhận.
     */
    @PostMapping(value = "/upload-variants", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadVariantImages(
            @RequestPart("variantIds") String variantIdsJson,
            @RequestParam MultiValueMap<String, MultipartFile> filesMap // Đổi tên biến để phản ánh Map
    ) throws IOException {

        // 1. **Phân tích Request và Ánh xạ File**
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> variantIds = objectMapper.readValue(variantIdsJson, new TypeReference<List<Long>>() {
        });
        log.info("Received variantIds: {}", variantIds);

        // 💡 TẠO MAP MỚI CHỨA DỮ LIỆU ĐÃ CHUYỂN ĐỔI (byte[])
        Map<Long, List<UploadFileDTO>> convertedVariantFilesMap = new HashMap<>();

        // Lặp qua TỪNG TÊN TRƯỜNG (Key: file_123, file_124)
        for (Map.Entry<String, List<MultipartFile>> entry : filesMap.entrySet()) {
            String partName = entry.getKey();
            List<MultipartFile> files = entry.getValue();

            if (partName.startsWith("file_")) {
                String variantIdStr = partName.substring("file_".length());
                try {
                    Long variantId = Long.parseLong(variantIdStr);

                    for (MultipartFile file : files) {
                        if (file != null && !file.isEmpty()) {
                            // <<< QUAN TRỌNG: ĐỌC DỮ LIỆU FILE VÀO byte[] TRÊN LUỒNG CHÍNH
                            UploadFileDTO dto = new UploadFileDTO(
                                    file.getOriginalFilename(),
                                    file.getContentType(),
                                    file.getBytes()
                            );
                            convertedVariantFilesMap
                                    .computeIfAbsent(variantId, id -> new ArrayList<>())
                                    .add(dto);
                        }
                    }
                } catch (NumberFormatException e) {
                    log.warn("Không parse được variantId từ partName: {}", partName);
                }
            } else {
                log.warn("Part name không đúng định dạng: {}", partName);
            }
        }

        for (Map.Entry<Long, List<UploadFileDTO>> entry : convertedVariantFilesMap.entrySet()) {
            Long varId = entry.getKey();
            List<UploadFileDTO> dtos = entry.getValue();
            log.info("Variant ID {} có {} file(s):", varId, dtos.size());
            for (UploadFileDTO dto : dtos) {
                // Log tên file, kích thước (byte[]) và kiểu (contentType)
                log.info("  - File: {} (size: {} bytes, type: {})",
                        dto.originalFilename(),
                        dto.content().length,
                        dto.contentType());
            }
        }

        // 2. **GỌI SERVICE VỚI MAP ĐÃ CHUYỂN ĐỔI**
        // Phương thức processBatchImageUpload cũng phải thay đổi tham số đầu vào.
        variantImageService.processBatchImageUpload(convertedVariantFilesMap);

        // 3. **Trả về Response** - Ngay lập tức
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body("Tác vụ tải lên ảnh đã được nhận và đang xử lý...");
    }
}

