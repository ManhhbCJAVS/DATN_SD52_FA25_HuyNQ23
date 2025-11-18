package edu.poly.datn_sd52_fa25_huynq203.library.service.coupon;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.coupon.attribute.CouponDTO;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon.Coupon;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface CouponService {
    CouponDTO createCoupon(CouponDTO couponDTO);
    CouponDTO updateCoupon(Long id, CouponDTO couponDTO);
    void deleteCoupon(Long id);
    CouponDTO getCouponById(Long id);
    List<CouponDTO> getAllCoupons();
    Page<CouponDTO> searchCoupons(String keyword, Pageable pageable);


}
