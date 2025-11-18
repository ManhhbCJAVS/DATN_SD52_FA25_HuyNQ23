package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.coupon.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CouponStatus;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@Builder
public class CouponDTO {
    Long id;

    @NotBlank(message = "Tên giảm giá không được để trống")
    String name;

    @NotBlank(message = "Loại giảm giá không được để trống (ví dụ: PERCENT hoặc AMOUNT)")
    String discountType;

    @NotNull(message = "Giá trị giảm không được để trống")
    @Min(value = 1, message = "Giá trị giảm phải lớn hơn 0")
    String discountValue;

    @NotNull(message = "Điều kiện tối thiểu không được để trống")
    @Min(value = 0, message = "Điều kiện tối thiểu không thể âm")
    Integer minimumCondition;

    @PositiveOrZero(message = "Số lượng mã phải >= 0")
    int couponCount;

    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
    LocalDateTime startDate;

    @Future(message = "Ngày kết thúc phải nằm trong tương lai")
    LocalDateTime endDate;

    CouponStatus status;
}
