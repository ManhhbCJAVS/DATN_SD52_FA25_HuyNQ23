package edu.poly.datn_sd52_fa25_huynq203.library.service;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.EmployeeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.EmployeeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    
    EmployeeResponse createEmployee(EmployeeRequest request);
    
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    
    EmployeeResponse getEmployeeById(Long id);
    
    void deleteEmployee(Long id);
    
    Page<EmployeeResponse> searchEmployees(String keyword, EmployeeStatus status, Role role, Pageable pageable);
    
    List<EmployeeResponse> getAllEmployees();
    
    EmployeeResponse changeStatus(Long id, EmployeeStatus status);
    
    EmployeeResponse login(String email, String password);
}
