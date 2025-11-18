package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.variant.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Size;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.BaseAttributeMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SizeMapper extends BaseAttributeMapper<
        Size,
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse
        > {
}
