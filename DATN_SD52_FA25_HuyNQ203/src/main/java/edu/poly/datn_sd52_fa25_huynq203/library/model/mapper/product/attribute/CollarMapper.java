package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Collar;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CollarMapper extends BaseAttributeMapper<
        Collar,
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse
        > {
}
