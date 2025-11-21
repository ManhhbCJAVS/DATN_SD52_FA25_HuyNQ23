package edu.poly.datn_sd52_fa25_huynq203.library.repository.address;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.address.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long> {
    List<Ward> findByDistrict_Id(Long districtId);
}

