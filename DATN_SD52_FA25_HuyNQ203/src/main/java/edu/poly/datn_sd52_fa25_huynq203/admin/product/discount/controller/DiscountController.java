package edu.poly.datn_sd52_fa25_huynq203.admin.product.discount.controller;
import edu.poly.datn_sd52_fa25_huynq203.admin.product.discount.payload.DiscountRequest;
import edu.poly.datn_sd52_fa25_huynq203.admin.product.discount.payload.DiscountResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.ResponseData;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.DiscountStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.discount.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/discount")
@RequiredArgsConstructor
//@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Validated
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping
    public ResponseData<List<DiscountResponse>> getAll(){
        List<DiscountResponse> discountResponses = discountService.getAll();
        return ResponseData.<List<DiscountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("lấy danh sách discount thành công")
                .data(discountResponses)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseData<DiscountResponse> getAll(@PathVariable(name = "id") Long id){
        DiscountResponse discountResponses = discountService.findById(id);
        return ResponseData.<DiscountResponse>builder()
                .status(HttpStatus.OK.value())
                .message("tìm thấy phiếu giảm giá thành công")
                .data(discountResponses)
                .build();
    }

    @GetMapping("/search")
    public ResponseData<List<DiscountResponse>> findByName(@RequestParam String name) {
        List<DiscountResponse> discounts = discountService.findByName(name);
        return ResponseData.<List<DiscountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("tìm thấy phiếu giảm giá thành công")
                .data(discounts)
                .build();
    }

    @GetMapping("/status")
    public ResponseData<List<DiscountResponse>> findByStatus(@RequestParam DiscountStatus status) {
        List<DiscountResponse> discounts = discountService.findByStatus(status);
        return ResponseData.<List<DiscountResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("lấy danh sách phiếu giảm giá thành công")
                .data(discounts)
                .build();
    }

    @PostMapping("/create")
    public ResponseData<DiscountResponse> create(@Valid @RequestBody DiscountRequest discountRequest) {
        DiscountResponse discountResponse = discountService.create(discountRequest);
        return ResponseData.<DiscountResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Thêm thành công")
                .data(discountResponse)
                .build();
    }


    @PutMapping("/{id}")
    public ResponseData<Void> updateDiscount(@Valid @RequestBody DiscountRequest request,@PathVariable(name = "id") Long id){
        discountService.updateDiscount(request,id);
        return ResponseData.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("Cập nhật Đợt giảm giá thành công")
                .build();
    }


}
