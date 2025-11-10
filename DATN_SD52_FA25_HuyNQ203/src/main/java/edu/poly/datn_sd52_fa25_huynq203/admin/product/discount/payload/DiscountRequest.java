package edu.poly.datn_sd52_fa25_huynq203.admin.product.discount.payload;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.*;

import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    @NotBlank(message = "tên của đợt giảm giá không được trống")
    String name;


    String code;

    @NotNull(message = "Phần trăm giảm giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Phần trăm giảm giá phải lớn hơn 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Phần trăm giảm giá không được vượt quá 100%")
    Double discountPercentage;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc trong tương lai")
    LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải ở trong tương lai")
    LocalDateTime endDate;

    List<Long> productVariantIds;

}