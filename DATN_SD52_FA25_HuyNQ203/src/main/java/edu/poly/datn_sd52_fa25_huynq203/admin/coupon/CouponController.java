package edu.poly.datn_sd52_fa25_huynq203.admin.coupon;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.coupon.CouponDTO;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.ResponseData;
import edu.poly.datn_sd52_fa25_huynq203.library.service.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${backoffice.endpoint}/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<ResponseData<CouponDTO>> createCoupon(@Valid @RequestBody CouponDTO couponDTO) {
        CouponDTO result = couponService.createCoupon(couponDTO);
        return ResponseEntity.ok(new ResponseData<>(200, "Create coupon success", result));
    }

    @GetMapping
    public ResponseEntity<ResponseData<?>> getAllCoupons() {
        return ResponseEntity.ok(new ResponseData<>(200, "Success", couponService.getAllCoupons()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<?>> getCouponById(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseData<>(200, "Success", couponService.getCouponById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<CouponDTO>> updateCoupon(
            @PathVariable Long id,
            @Valid @RequestBody CouponDTO couponDTO) {

        CouponDTO updated = couponService.updateCoupon(id, couponDTO);
        return ResponseEntity.ok(new ResponseData<>(200, "Update success", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<?>> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(new ResponseData<>(200, "Delete success", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseData<Page<CouponDTO>>> searchCoupons(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CouponDTO> result = couponService.searchCoupons(keyword, (java.awt.print.Pageable) pageable);

        return ResponseEntity.ok(new ResponseData<>(200, "Search success", result));
    }
}
