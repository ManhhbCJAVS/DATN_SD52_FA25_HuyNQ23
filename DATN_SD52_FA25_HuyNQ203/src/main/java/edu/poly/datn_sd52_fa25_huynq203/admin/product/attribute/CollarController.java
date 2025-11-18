package edu.poly.datn_sd52_fa25_huynq203.admin.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Collar;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.CollarMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product.attribute.AbstractBaseAttributeService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product.attribute.CollarService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/collars")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
@Slf4j
public class CollarController extends AbstractBaseAttributeController<
        Collar,
        Long,
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse,
        CollarMapper
        > {
    
    CollarService collarService;

    @Override
    protected AbstractBaseAttributeService<
            Collar,
            Long,
            CreateAttributeRequest,
            UpdateAttributeRequest,
            CommonAttributeResponse,
            CollarMapper
            > getService() {
        return collarService;
    }
}
