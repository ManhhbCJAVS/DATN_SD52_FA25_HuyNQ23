package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.CreateVariantRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.UpdateVariantRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ProductVariantResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {
    ProductVariant convertRequestToEntity(CreateVariantRequest createVariantRequest);

    List<ProductVariant> convertRequestsToEntities(List<CreateVariantRequest> createVariantRequests);

    // --- HÀM MỚI CHO PATCH UPDATE ---
    // MapStruct: update field != null trong dto -> entity.
    // @MappingTarget: obj cần đc update.
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVariantFromDto(UpdateVariantRequest dto, @MappingTarget ProductVariant entity);

    ProductVariantResponse toResponseBasic(ProductVariant variant);

    List<ProductVariantResponse> toListResponseBasic(List<ProductVariant> variants);

}
