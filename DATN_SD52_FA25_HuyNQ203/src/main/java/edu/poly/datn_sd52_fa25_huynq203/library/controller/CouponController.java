package edu.poly.datn_sd52_fa25_huynq203.library.controller;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.coupon.attribute.CouponDTO;
import edu.poly.datn_sd52_fa25_huynq203.library.service.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private  CouponDTO  couponDTO;

    @PostMapping
    public ResponseEntity<CouponDTO> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        return ResponseEntity.ok(couponService.createCoupon(couponDTO));
    }

    @GetMapping
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDTO> getCouponById(@PathVariable Long id) {
        return ResponseEntity.ok(couponService.getCouponById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponDTO> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponDTO couponDTO) {
        return ResponseEntity.ok(couponService.updateCoupon(id, couponDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CouponDTO>> searchCoupons(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(couponService.searchCoupons(keyword, (java.awt.print.Pageable) pageable));
    }
}
