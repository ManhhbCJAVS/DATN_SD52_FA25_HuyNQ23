package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateColorRequest extends CreateAttributeRequest {
    // Thêm trường đặc thù của Color
    @NotBlank(message = "Hex code cannot be blank")
    private String hexCode;
}