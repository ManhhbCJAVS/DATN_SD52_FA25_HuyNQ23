package edu.poly.datn_sd52_fa25_huynq203.library.service.impl;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.coupon.attribute.CouponDTO;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon.Coupon;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CouponStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.CouponMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.coupon.attribute.CouponRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    public CouponDTO createCoupon(CouponDTO dto) {

        Coupon coupon = couponMapper.toEntity(dto);
        coupon.setCreatedAt(LocalDateTime.now());
        if (coupon.getStatus() == null) {
            coupon.setStatus(CouponStatus.ACTIVE);
        }

        return couponMapper.toDTO(couponRepository.save(coupon));
    }

    @Override
    public CouponDTO updateCoupon(Long id, CouponDTO dto) {
        Coupon existing = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy coupon ID: " + id));

        existing.setName(dto.getName());
        existing.setDiscountType(dto.getDiscountType());
        existing.setDiscountValue(String.valueOf(dto.getDiscountValue()));
        existing.setMinimumCondition(dto.getMinimumCondition());
        existing.setCouponCount(dto.getCouponCount());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setStatus(dto.getStatus());

        return couponMapper.toDTO(couponRepository.save(existing));
    }

    @Override
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy coupon ID: " + id);
        }
        couponRepository.deleteById(id);
    }

    @Override
    public CouponDTO getCouponById(Long id) {
        return couponRepository.findById(id)
                .map(couponMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy coupon ID: " + id));
    }

    @Override
    public List<CouponDTO> getAllCoupons() {
        return couponRepository.findAll().stream()
                .map(couponMapper::toDTO)
                .toList();
    }

    @Override
    public Page<CouponDTO> searchCoupons(String keyword, java.awt.print.Pageable pageable) {
        return null;
    }

//    @Override
//    public Page<CouponDTO> searchCoupons(String keyword, Pageable pageable) {
//        return couponRepository
//                .findByCodeContainingIgnoreCase(keyword == null ? "" : keyword, pageable)
//                .map(couponMapper::toDTO);
//    }
}
