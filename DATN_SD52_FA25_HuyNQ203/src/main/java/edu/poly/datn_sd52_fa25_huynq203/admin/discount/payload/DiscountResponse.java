package edu.poly.datn_sd52_fa25_huynq203.admin.discount.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponse {

    Long id;

    String name;

    Double discountPercentage;

//     tuỳ thuộc vào cách mà có ý định làm theo hướng giảm giá điều kiện giả cả bao nhiêu
//     Double priceThreshold;

    String status;

    LocalDateTime startDate;

    LocalDateTime endDate;

    // cái này tuỳ thuộc vào cách tra ra của người ấy sản phẩm
    List<ProductVariant> productVariants;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = lombok.AccessLevel.PRIVATE)
    public static class ProductVariant {
        Long id;
        String name;
        Double originalPrice;
        Double finalPrice;
    }

}
