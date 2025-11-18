package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Origin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OriginMapper extends BaseAttributeMapper<
        Origin,
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse
        > {
}
