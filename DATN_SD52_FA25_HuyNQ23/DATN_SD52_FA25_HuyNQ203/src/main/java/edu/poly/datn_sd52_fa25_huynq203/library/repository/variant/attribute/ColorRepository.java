package edu.poly.datn_sd52_fa25_huynq203.library.repository.variant.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
}
