package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.discount;

import edu.poly.datn_sd52_fa25_huynq203.admin.product.discount.payload.DiscountRequest;
import edu.poly.datn_sd52_fa25_huynq203.admin.product.discount.payload.DiscountResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.DiscountStatus;

import java.util.List;

public interface DiscountService {

    DiscountResponse create(DiscountRequest discountRequest);

    List<DiscountResponse> getAll();


    DiscountResponse findById(Long id);


    void updateDiscount(DiscountRequest request, Long id);


    List<DiscountResponse> findByName(String name);

    List<DiscountResponse> findByStatus(DiscountStatus status);

}
