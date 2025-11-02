package edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

}
