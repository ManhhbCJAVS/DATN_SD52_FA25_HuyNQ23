package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
public class ProductRequest {

    @NotBlank(message = "Brand name is required")
    @Schema(description = "Tên sản phẩm", example = "Áo thun nam", requiredMode = Schema.RequiredMode.REQUIRED)
    String name;


    @NotNull(message = "Brand ID is required")
    @Min(value = 1, message = "Brand ID must be greater than 0")
    @Schema(description = "ID thương hiệu", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long brandId;

    public void setName(String name) {
        this.name = (name == null) ? null : name.trim().replaceAll("\\s+", " ");
    }

}
