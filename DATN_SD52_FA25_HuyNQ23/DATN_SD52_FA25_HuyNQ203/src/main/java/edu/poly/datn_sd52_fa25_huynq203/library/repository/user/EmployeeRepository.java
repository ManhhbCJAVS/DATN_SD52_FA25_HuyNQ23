package edu.poly.datn_sd52_fa25_huynq203.library.repository.user;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Employee;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByCode(String code);
    
    Optional<Employee> findByEmail(String email);
    
    Optional<Employee> findByPhone(String phone);
    
    boolean existsByCode(String code);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR e.status = :status) AND " +
           "(:role IS NULL OR e.role = :role)")
    Page<Employee> searchEmployees(@Param("keyword") String keyword, 
                                   @Param("status") EmployeeStatus status,
                                   @Param("role") Role role,
                                   Pageable pageable);
}
