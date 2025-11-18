package edu.poly.datn_sd52_fa25_huynq203.admin.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.ResponseData;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.CommonAttribute;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.BaseAttributeMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product.attribute.AbstractBaseAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;

public abstract class AbstractBaseAttributeController<
        E extends CommonAttribute,
        ID extends Serializable,
        CR extends CreateAttributeRequest,
        UR extends UpdateAttributeRequest,
        R extends CommonAttributeResponse,
        M extends BaseAttributeMapper<E, CR, UR, R>
        > {
    // Abstract method buộc lớp con cung cấp Service
    protected abstract AbstractBaseAttributeService<E, ID, CR, UR, R, M> getService();

    // 1. POST / (Tạo mới)
    @PostMapping()
    public ResponseData<R> create(
            @Valid @RequestBody CR request
    ) {
        return new ResponseData<>(
                HttpStatus.CREATED.value(),
                "CREATE SUCCESSFULLY",
                getService().create(request)
        );
    }

    // 2. PATCH /{id} (Cập nhật/Xóa mềm/Khôi phục)
    @PatchMapping("/{id}")
    public ResponseData<R> update(
            @PathVariable("id") ID id,
            @Valid @RequestBody UR request
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "UPDATE SUCCESSFULLY",
                getService().update(id, request)
        );
    }
    // ... Các endpoint chung khác như GET, DELETE (cho xóa cứng)

    @GetMapping
    @Operation(
            summary = "Tìm kiếm, lọc và phân trang Thuộc tính",
            description = "Tìm kiếm các Thuộc tính theo tham số lọc (`filterParams`). Hỗ trợ chế độ phân trang (mặc định) hoặc lấy toàn bộ (`disablePaging=true`)."
    )
    public ResponseData<?> find(
            @RequestParam(required = false, defaultValue = "false")
            boolean disablePaging,
            @ParameterObject
            @PageableDefault(size = 5, sort = "id")
            Pageable pageable,
            @Parameter(example = "[\"code:BR*\",\"isDeleted:FALSE\", \"name:MetroStyle\"]")
            String[] filterParams
    ) {
        return new ResponseData<>(
                HttpStatus.OK.value(),
                "FIND ATTRIBUTES SUCCESSFULLY",
                getService().findBySpecifications( // Gọi phương thức đã có ở AbstractBaseAttributeService
                        disablePaging,
                        pageable,
                        filterParams
                )
        );
    }

}
