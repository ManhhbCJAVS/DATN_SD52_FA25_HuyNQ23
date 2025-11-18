package edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import java.util.List;

public interface BaseAttributeMapper<E, CR, UR, R> {

    E mapCreateRequestToEntity(CR request);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateRequestToEntity(UR request, @MappingTarget E entity);


    R mapEntityToResponse(E entity);


    List<R> mapEntitiesToResponses(List<E> entities);
}