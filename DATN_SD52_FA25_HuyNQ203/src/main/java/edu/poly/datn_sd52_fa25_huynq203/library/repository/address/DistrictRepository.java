package edu.poly.datn_sd52_fa25_huynq203.library.repository.address;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.address.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findByProvince_Id(Long provinceId);
}

