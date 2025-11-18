package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product;


import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.CreateVariantRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
public class CreateProductRequest {

    @Schema(description = "Thông tin dòng sản phẩm", required = true)
    @Valid
    ProductRequest productRequest;

    @NotEmpty(message = "Variants list must not be empty")
    @Valid
    @Schema(description = "Danh sách các biến thể sản phẩm", required = true)
    List<CreateVariantRequest> variants;
}
