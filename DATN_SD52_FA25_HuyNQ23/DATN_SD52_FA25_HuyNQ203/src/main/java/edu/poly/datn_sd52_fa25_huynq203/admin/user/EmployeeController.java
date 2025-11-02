package edu.poly.datn_sd52_fa25_huynq203.admin.user;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.EmployeeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.EmployeeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Role;
import edu.poly.datn_sd52_fa25_huynq203.library.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/employees")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) Role role,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<EmployeeResponse> employeePage = employeeService.searchEmployees(keyword, status, role, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("employees", employeePage.getContent());
        response.put("currentPage", employeePage.getNumber());
        response.put("totalItems", employeePage.getTotalElements());
        response.put("totalPages", employeePage.getTotalPages());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployeesNoPaging() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }
    
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse employee = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request
    ) {
        EmployeeResponse employee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(employee);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Xóa nhân viên thành công");
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<EmployeeResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam EmployeeStatus status
    ) {
        EmployeeResponse employee = employeeService.changeStatus(id, status);
        return ResponseEntity.ok(employee);
    }
    
    @PostMapping("/login")
    public ResponseEntity<EmployeeResponse> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        EmployeeResponse employee = employeeService.login(email, password);
        return ResponseEntity.ok(employee);
    }
}
