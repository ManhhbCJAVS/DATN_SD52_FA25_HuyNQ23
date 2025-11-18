package edu.poly.datn_sd52_fa25_huynq203.admin.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Material;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.MaterialMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product.attribute.AbstractBaseAttributeService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product.attribute.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/materials")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
@Slf4j
public class MaterialController extends AbstractBaseAttributeController<
        Material,
        Long,
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse,
        MaterialMapper
        > {
    
    MaterialService materialService;

    @Override
    protected AbstractBaseAttributeService<
            Material,
            Long,
            CreateAttributeRequest,
            UpdateAttributeRequest,
            CommonAttributeResponse,
            MaterialMapper
            > getService() {
        return materialService;
    }
}
