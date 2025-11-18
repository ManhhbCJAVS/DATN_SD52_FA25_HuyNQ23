package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.ProductRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.UpdateProductRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.product.ProductResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.Product;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
//, uses = {ProductVariantMapper.class}
@Mapper(componentModel = "spring"  )
public interface ProductMapper {
    Product convertUpdateRequestToEntity(UpdateProductRequest updateProductRequest);
    Product convertRequestToEntity(ProductRequest productRequest);

    @Named("basic")
    @Mapping(target = "variantResponses", ignore = true)
    ProductResponse toResponseBasic(Product product);

    @IterableMapping(qualifiedByName = "basic")
    List<ProductResponse> toListResponseBasic(List<Product> products);

//    @Named("full")
//    ProductResponse toResponseFull(Product product);
//
//    @IterableMapping(qualifiedByName = "full")
//    List<ProductResponse> toListResponseFull(List<Product> products);
}
