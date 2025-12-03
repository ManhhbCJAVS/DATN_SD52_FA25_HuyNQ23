package edu.poly.datn_sd52_fa25_huynq203.admin.product;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.CreateProductRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.UpdateProductRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.ResponseData;
import edu.poly.datn_sd52_fa25_huynq203.library.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${backoffice.endpoint}/products")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Validated
@Slf4j
public class ProductController {
    ProductService productService;

    @Operation(
            summary = "Cập nhật trạng thái Sản phẩm",
            tags = {"Product"}
    )
    @PatchMapping()
    public ResponseData<?> updateStatusProduct(
            @Valid @RequestBody UpdateProductRequest updateProductRequest
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "UPDATE PRODUCT SUCCESSFULLY",
                productService.updateProductRequest(updateProductRequest)
        );
    }

    @Operation(
            summary = "Tạo Sản phẩm và các Biến thể ban đầu",
            description = "Thực hiện tạo một Sản phẩm mới cùng với danh sách các Biến thể (Variant) đi kèm trong cùng một Request.",
            tags = {"Product"}
    )
    @PostMapping()
    public ResponseData<?> createProduct(
            @Valid @RequestBody CreateProductRequest createProductRequest
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "CREATE PRODUCT SUCCESSFULLY",
                productService.createProductRequest(
                        createProductRequest.getProductRequest(),
                        createProductRequest.getVariants()
                )
        );
    }

    @Operation(
            summary = "Tìm kiếm, lọc và phân trang Sản phẩm (Product)",
            description = "Tìm kiếm các Sản phẩm theo các tham số lọc (`filterParams`). Hỗ trợ chế độ phân trang (mặc định) hoặc lấy toàn bộ (`disablePaging=true`).",
            tags = {"Product"}
    )
    @GetMapping
    public ResponseData<?> findProducts(
            // THÊM THAM SỐ TÙY CHỌN: nếu FE gửi unpaged=true, ta sẽ bỏ qua Pageable
            @RequestParam(required = false, defaultValue = "false")
            Boolean disablePaging,
            @ParameterObject
            @PageableDefault(size = 14, sort = "id")
            Pageable pageable,
            @Parameter(
                    example = "[\"code:PRD*\"]",
                    description = " name:*Laptop*  |  status:ACTIVE | status!INACTIVE"
            )
            String[] filterParams
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "FIND PRODUCTS SUCCESSFULLY",
                productService.findBySpecifications(
                        disablePaging != null && disablePaging,
                        pageable,
                        filterParams
                )
        );
    }
}