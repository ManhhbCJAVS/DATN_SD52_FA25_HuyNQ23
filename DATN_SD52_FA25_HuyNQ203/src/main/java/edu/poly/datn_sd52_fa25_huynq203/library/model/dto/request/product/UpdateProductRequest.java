package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UpdateProductRequest {
    @NotNull(message = "Product ID không được để trống")
    @Min(value = 1, message = "Product ID phải là số dương")
    Long id;
    @NotNull(message = "Trạng thái sản phẩm không được để trống (null)")
    ProductStatus status;
}
