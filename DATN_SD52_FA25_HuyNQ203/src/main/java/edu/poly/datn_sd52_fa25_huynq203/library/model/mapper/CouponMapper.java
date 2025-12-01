package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.coupon.CouponDTO;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon.Coupon;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public Coupon toEntity(CouponDTO dto) {
        if (dto == null) return null;
        return Coupon.builder()
                .id(dto.getId())
                .name(dto.getName())
                .discountType(dto.getDiscountType())
                .discountValue(dto.getDiscountValue())
                .minimumCondition(dto.getMinimumCondition())
                .couponCount(dto.getCouponCount())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .build();
    }

    public CouponDTO toDTO(Coupon entity) {
        if (entity == null) return null;
        return CouponDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .discountType(entity.getDiscountType())
                .discountValue(entity.getDiscountValue())
                .minimumCondition(entity.getMinimumCondition())
                .couponCount(entity.getCouponCount())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .build();
    }
}
