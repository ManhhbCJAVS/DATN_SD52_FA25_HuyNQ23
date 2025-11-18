package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateVariantRequest {
    @NotNull(message = "Variant ID is required for update")
    @Min(value = 1, message = "Variant ID must be greater than 0")
    @Schema(description = "ID của biến thể cần cập nhật", example = "101")
    Long variantId;

    @Min(value = 1, message = "Price must be greater than 0")
    @Schema(description = "Đơn giá mới của biến thể", example = "249")
    Double price; // Dùng Double để có thể là null

    @Min(value = 0, message = "Quantity cannot be negative")
    @Schema(description = "Số lượng tồn kho mới", example = "50")
    Integer quantity; // Dùng Integer để có thể là null

    @Schema(description = "Mô tả mới của biến thể" , example = "Phiên bản mới, không còn lỗi")
    String description; // Dùng String để có thể là null

    @Schema(description = "Trạng thái mới của biến thể" , example = "ACTIVE")
    ProductStatus status;
}