package edu.poly.datn_sd52_fa25_huynq203.library.repository.address;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.address.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
}
