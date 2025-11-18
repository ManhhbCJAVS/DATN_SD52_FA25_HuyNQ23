package edu.poly.datn_sd52_fa25_huynq203.library.repository.variant.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Gender;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends CommonAttributeRepository<Gender, Long> {
}
