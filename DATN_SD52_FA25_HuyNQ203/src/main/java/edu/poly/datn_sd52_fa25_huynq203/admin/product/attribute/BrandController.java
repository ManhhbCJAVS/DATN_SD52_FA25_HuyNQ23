package edu.poly.datn_sd52_fa25_huynq203.admin.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Brand;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.BrandMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute.BrandService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute.AbstractBaseAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${backoffice.endpoint}/brands")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Validated
@Slf4j
public class BrandController extends AbstractBaseAttributeController<
        Brand,
        Long,
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse,
        BrandMapper
        > {
    BrandService brandService; //Inject By Spring IoC Container

    @Override
    protected AbstractBaseAttributeService<
            Brand,
            Long,
            CreateAttributeRequest,
            UpdateAttributeRequest,
            CommonAttributeResponse,
            BrandMapper
            > getService() {
        return brandService;
    }
}