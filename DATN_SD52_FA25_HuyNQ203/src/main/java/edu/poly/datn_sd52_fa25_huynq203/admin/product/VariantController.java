package edu.poly.datn_sd52_fa25_huynq203.admin.product;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.CreateVariantRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.UpdateVariantRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.ResponseData;
import edu.poly.datn_sd52_fa25_huynq203.library.service.variant.ProductVariantService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${backoffice.endpoint}/products")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Validated
@Slf4j
public class VariantController {
    ProductVariantService productVariantService;


    @Operation(
            summary = "Cập nhật thông tin chi tiết của một hoặc nhiều Biến thể",
            description = "Thực hiện cập nhật từng phần (Patch Semantic) cho danh sách các biến thể. Chỉ cập nhật Price, Quantity, Description, Status nếu chúng được gửi kèm trong Request Body.",
            tags = {"Variant"}
    )
    @PatchMapping("/{productId}")
    public ResponseData<?> updateVariants(
            @Valid @RequestBody List<UpdateVariantRequest> updateVariantRequests
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "UPDATE VARIANTS SUCCESSFULLY",
                productVariantService.updateVariantRequests(updateVariantRequests)
        );
    }

    @Operation(
            summary = "Tạo Biến thể mới (Variant) cho Sản phẩm hiện tại",
            description = "Thêm một danh sách các biến thể (dựa trên Price, Quantity, Color,...) cho Sản phẩm đã tồn tại (dựa trên productId).",
            tags = {"Variant"}
    )
    @PostMapping("/{productId}")
    public ResponseData<?> createVariant(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody List<CreateVariantRequest> createVariantRequests
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "CREATE PRODUCT SUCCESSFULLY",
                productVariantService.createVariantRequest(
                        productId,
                        createVariantRequests
                )
        );
    }

    @Operation(
            summary = "Tìm kiếm và lọc các Biến thể (Variant) của một Sản phẩm",
            description = "Tìm kiếm các biến thể theo các tham số lọc (`filterParams`). Hỗ trợ chế độ phân trang (mặc định) hoặc lấy toàn bộ (`disablePaging=true`).",
            tags = {"Variant"}
    )
    @GetMapping("/{productId}")
    public ResponseData<?> findVariants(
            // THÊM THAM SỐ TÙY CHỌN: nếu FE gửi unpaged=true, ta sẽ bỏ qua Pageable
            @RequestParam(required = false, defaultValue = "false")
            Boolean disablePaging,
            @ParameterObject
            // Default: page = 0, direction = Sort.Direction.ASC
            @PageableDefault(size = 14, sort = "id")
            Pageable pageable,
            @Parameter(
                    example = "[\"code:PR-VAR*\"]",
                    description = " Thêm productId trong tham số"
            )
            String[] filterParams
    ) {
        return new ResponseData<>(

                HttpStatus.OK.value(),
                "FIND VARIANTS SUCCESSFULLY",
                productVariantService.findBySpecifications(
                        disablePaging != null && disablePaging,
                        pageable,
                        filterParams
                )
        );
    }

}
