package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.variant.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color.CreateColorRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color.UpdateColorRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ColorResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Color;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.BaseAttributeMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ColorMapper extends BaseAttributeMapper<
        Color,
        CreateColorRequest,
        UpdateColorRequest,
        ColorResponse
        > {
}
