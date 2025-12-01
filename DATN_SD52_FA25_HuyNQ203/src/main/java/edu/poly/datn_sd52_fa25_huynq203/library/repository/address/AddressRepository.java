package edu.poly.datn_sd52_fa25_huynq203.library.repository.address;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomer_Id(Long customerId);
    List<Address> findByEmployee_Id(Long employeeId);
}
