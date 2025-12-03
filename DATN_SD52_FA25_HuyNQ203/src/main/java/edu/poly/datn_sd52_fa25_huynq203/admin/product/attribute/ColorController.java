package edu.poly.datn_sd52_fa25_huynq203.admin.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color.CreateColorRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color.UpdateColorRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ColorResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Color;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.variant.attribute.ColorMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute.AbstractBaseAttributeService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.variant.attribute.ColorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${backoffice.endpoint}/colors")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Validated
@Slf4j
public class ColorController extends AbstractBaseAttributeController<
        Color,
        Long,
        CreateColorRequest,
        UpdateColorRequest,
        ColorResponse,
        ColorMapper
        > {
    
    ColorService colorService;

    @Override
    protected AbstractBaseAttributeService<
            Color,
            Long,
            CreateColorRequest,
            UpdateColorRequest,
            ColorResponse,
            ColorMapper
            > getService() {
        return colorService;
    }
}