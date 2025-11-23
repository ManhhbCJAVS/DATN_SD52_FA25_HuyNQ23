package edu.poly.datn_sd52_fa25_huynq203.library.service.address;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.AddressRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.address.AddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(AddressRequest request);
    AddressResponse updateAddress(Long id, AddressRequest request);
    void deleteAddress(Long id);
    AddressResponse getAddressById(Long id);
    List<AddressResponse> getAddressesByCustomer(Long customerId);
    List<AddressResponse> getAddressesByEmployee(Long employeeId);
    List<AddressResponse> getAllAddresses();
    Page<AddressResponse> searchAddresses(String keyword, Pageable pageable);
}