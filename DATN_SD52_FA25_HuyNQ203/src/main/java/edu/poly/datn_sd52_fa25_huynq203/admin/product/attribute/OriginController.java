package edu.poly.datn_sd52_fa25_huynq203.admin.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Origin;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.OriginMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute.AbstractBaseAttributeService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute.OriginService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${backoffice.endpoint}/origins")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
@Slf4j
public class OriginController extends AbstractBaseAttributeController<
        Origin,
        Long,
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse,
        OriginMapper
        > {
    
    OriginService originService;

    @Override
    protected AbstractBaseAttributeService<
            Origin,
            Long,

            CreateAttributeRequest,
            UpdateAttributeRequest,
            CommonAttributeResponse,
            OriginMapper
            > getService() {
        return originService;
    }
}
