package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.discount.impl;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ResourceNotFoundException;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.AddressRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.address.AddressResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.address.*;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.address.*;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.user.*;
import edu.poly.datn_sd52_fa25_huynq203.library.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;

    // CREATE
    @Override
    @Transactional
    public AddressResponse createAddress(AddressRequest request) {
        Address address = Address.builder()
                .customer(request.getCustomerId() != null
                        ? customerRepository.findById(request.getCustomerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng"))
                        : null)
                .employee(request.getEmployeeId() != null
                        ? employeeRepository.findById(request.getEmployeeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên"))
                        : null)
                .province(provinceRepository.findById(request.getProvinceId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tỉnh/thành phố")))
                .district(districtRepository.findById(request.getDistrictId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quận/huyện")))
                .ward(wardRepository.findById(request.getWardId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phường/xã")))
                .isDefault(request.isDefault())
                .build();

        Address saved = addressRepository.save(address);
        return mapToResponse(saved);
    }

    // UPDATE
    @Override
    @Transactional
    public AddressResponse updateAddress(Long id, AddressRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ có ID: " + id));

        if (request.getProvinceId() != null)
            address.setProvince(provinceRepository.findById(request.getProvinceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tỉnh/thành phố")));

        if (request.getDistrictId() != null)
            address.setDistrict(districtRepository.findById(request.getDistrictId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quận/huyện")));

        if (request.getWardId() != null)
            address.setWard(wardRepository.findById(request.getWardId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phường/xã")));

        address.setCustomer(request.getCustomerId() != null
                ? customerRepository.findById(request.getCustomerId()).orElse(null)
                : null);

        address.setEmployee(request.getEmployeeId() != null
                ? employeeRepository.findById(request.getEmployeeId()).orElse(null)
                : null);

        address.setDefault(request.isDefault());

        Address updated = addressRepository.save(address);
        return mapToResponse(updated);
    }

    // READ
    @Override
    public List<AddressResponse> getAddressesByCustomer(Long customerId) {
        return addressRepository.findByCustomer_Id(customerId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressResponse> getAddressesByEmployee(Long employeeId) {
        return addressRepository.findByEmployee_Id(employeeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddressById(Long id) {
        return addressRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ có ID: " + id));
    }

    // DELETE
    @Override
    @Transactional
    public void deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + id);
        }
        addressRepository.deleteById(id);
    }

    // SEARCH
    @Override
    public Page<AddressResponse> searchAddresses(String keyword, Pageable pageable) {
        Page<Address> page = addressRepository.findAll(pageable);
        List<AddressResponse> responses = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, page.getTotalElements());
    }

    // MAP
    private AddressResponse mapToResponse(Address a) {
        return AddressResponse.builder()
                .id(a.getId())
                .customerName(a.getCustomer() != null ? a.getCustomer().getName() : null)
                .employeeName(a.getEmployee() != null ? a.getEmployee().getName() : null)
                .provinceName(a.getProvince() != null ? a.getProvince().getName() : null)
                .districtName(a.getDistrict() != null ? a.getDistrict().getName() : null)
                .wardName(a.getWard() != null ? a.getWard().getName() : null)
                .isDefault(a.isDefault())
                .build();
    }
}

