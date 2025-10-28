package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

//TODO: Fields to add + Input validation + Business-validation
@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BrandRequest {
    @NotBlank(message = "Brand name must not be blank")
    @Size(max = 100, message = "Brand name must not exceed 100 characters")
    String name;
}
