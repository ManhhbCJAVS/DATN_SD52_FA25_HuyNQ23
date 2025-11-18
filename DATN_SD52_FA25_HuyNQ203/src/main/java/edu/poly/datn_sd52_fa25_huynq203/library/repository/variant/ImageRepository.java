package edu.poly.datn_sd52_fa25_huynq203.library.repository.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
