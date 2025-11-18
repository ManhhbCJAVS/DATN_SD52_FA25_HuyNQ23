package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
public class CreateVariantRequest {

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be greater than 0")
    @Schema(description = "Giá của biến thể sản phẩm", example = "199")
    Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater than 0")
    @Schema(description = "Số lượng tồn kho", example = "100")
    Integer quantity;

    @Schema(description = "Mô tả sản phẩm", example = "Áo thun cotton chất lượng cao")
    String description;

    @NotNull(message = "Color ID is required")
    @Min(value = 1, message = "Color ID must be greater than 0")
    @Schema(description = "ID màu sắc", example = "2")
    Long colorId;

//    Long sizeId;
//    Long genderId;
}
