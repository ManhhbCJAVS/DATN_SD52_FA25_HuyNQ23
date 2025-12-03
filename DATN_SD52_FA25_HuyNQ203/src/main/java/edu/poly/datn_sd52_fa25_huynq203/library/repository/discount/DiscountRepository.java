package edu.poly.datn_sd52_fa25_huynq203.library.repository.discount;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount.Discount;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.DiscountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    boolean existsByName(String name);

    List<Discount> findByNameContainingIgnoreCase(String name);

    List<Discount> findByStatus(DiscountStatus status);
}
