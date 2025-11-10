package edu.poly.datn_sd52_fa25_huynq203.library.repository;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}
