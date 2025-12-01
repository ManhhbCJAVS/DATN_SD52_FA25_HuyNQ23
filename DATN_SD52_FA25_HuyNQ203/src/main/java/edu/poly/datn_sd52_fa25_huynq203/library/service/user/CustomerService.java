package edu.poly.datn_sd52_fa25_huynq203.library.service.user;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CustomerRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.user.CustomerResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    CustomerResponse getCustomerById(Long id);

    void deleteCustomer(Long id);

    Page<CustomerResponse> searchCustomers(String keyword, CustomerStatus status, Pageable pageable);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse changeStatus(Long id, CustomerStatus status);
}

