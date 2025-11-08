package edu.poly.datn_sd52_fa25_huynq203.library.repository.coupon.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon.Coupon;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {





}
