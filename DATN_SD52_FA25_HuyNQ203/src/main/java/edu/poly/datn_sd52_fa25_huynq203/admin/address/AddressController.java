package edu.poly.datn_sd52_fa25_huynq203.admin.address;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.AddressRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.address.AddressResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.service.address.AddressService;
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
@RequestMapping("/api/admin/addresses")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AddressController {

    private final AddressService addressService;

    // ✅ Lấy danh sách địa chỉ có phân trang + tìm kiếm
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<AddressResponse> addressPage = addressService.searchAddresses(keyword, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("addresses", addressPage.getContent());
        response.put("currentPage", addressPage.getNumber());
        response.put("totalItems", addressPage.getTotalElements());
        response.put("totalPages", addressPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    // ✅ Lấy toàn bộ địa chỉ (không phân trang)
    @GetMapping("/all")
    public ResponseEntity<List<AddressResponse>> getAllAddressesNoPaging() {
        List<AddressResponse> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    // ✅ Lấy địa chỉ theo ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) {
        AddressResponse address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    // ✅ Lấy địa chỉ theo khách hàng
    @GetMapping("/customer/{id}")
    public ResponseEntity<List<AddressResponse>> getAddressesByCustomer(@PathVariable("id") Long customerId) {
        List<AddressResponse> addresses = addressService.getAddressesByCustomer(customerId);
        return ResponseEntity.ok(addresses);
    }

    // ✅ Lấy địa chỉ theo nhân viên
    @GetMapping("/employee/{id}")
    public ResponseEntity<List<AddressResponse>> getAddressesByEmployee(@PathVariable("id") Long employeeId) {
        List<AddressResponse> addresses = addressService.getAddressesByEmployee(employeeId);
        return ResponseEntity.ok(addresses);
    }

    // ✅ Tạo địa chỉ mới
    @PostMapping
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody AddressRequest request) {
        AddressResponse address = addressService.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }

    // ✅ Cập nhật địa chỉ
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request
    ) {
        AddressResponse address = addressService.updateAddress(id, request);
        return ResponseEntity.ok(address);
    }

    // ✅ Xóa địa chỉ
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Xóa địa chỉ thành công");
        return ResponseEntity.ok(response);
    }
}


