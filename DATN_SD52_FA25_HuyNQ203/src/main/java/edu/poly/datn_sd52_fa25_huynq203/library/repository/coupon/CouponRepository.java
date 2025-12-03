package edu.poly.datn_sd52_fa25_huynq203.library.repository.coupon;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {





}
