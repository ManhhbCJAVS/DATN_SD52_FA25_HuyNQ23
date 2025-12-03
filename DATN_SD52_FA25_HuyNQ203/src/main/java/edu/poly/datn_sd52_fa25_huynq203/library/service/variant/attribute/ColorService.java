package edu.poly.datn_sd52_fa25_huynq203.library.service.variant.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color.CreateColorRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color.UpdateColorRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ColorResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Color;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.variant.attribute.ColorMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.variant.attribute.ColorRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute.AbstractBaseAttributeService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ColorService extends AbstractBaseAttributeService<
        Color,
        Long,
        CreateColorRequest,
        UpdateColorRequest,
        ColorResponse,
        ColorMapper
        > {
    ColorRepository colorRepository;
    ColorMapper colorMapper;

    @Autowired
    public ColorService(
            ColorRepository colorRepository,
            ColorMapper colorMapper,
            SpecificationBuilderService specificationBuilderService
    ) {
        super(specificationBuilderService);
        this.colorRepository = colorRepository;
        this.colorMapper = colorMapper;
    }

    @Override
    protected CommonAttributeRepository<Color, Long> getCommonAttributeRepository() { // 👈 SỬA KIỂU TRẢ VỀ
        return colorRepository;
    }

    @Override
    protected ColorMapper getMapper() {
        return colorMapper;
    }
}