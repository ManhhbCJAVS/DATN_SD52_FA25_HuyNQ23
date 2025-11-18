package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Origin;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.OriginMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.OriginRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OriginService extends AbstractBaseAttributeService<
        Origin, // E: Entity
        Long, // ID: ID Type
        CreateAttributeRequest, // CR: Create Request Type
        UpdateAttributeRequest, // UR: Update Request Type
        CommonAttributeResponse, // R: Response Type
        OriginMapper
        > {
    OriginRepository originRepository;
    OriginMapper originMapper;

    @Autowired
    public OriginService(
            OriginRepository originRepository,
            OriginMapper originMapper,
            SpecificationBuilderService specificationBuilderService
    ) {
        super(specificationBuilderService);
        this.originRepository = originRepository;
        this.originMapper = originMapper;
    }

    @Override
    protected CommonAttributeRepository<Origin, Long> getCommonAttributeRepository() { // 👈 SỬA KIỂU TRẢ VỀ
        return originRepository;
    }


    @Override
    protected OriginMapper getMapper() {
        return originMapper;
    }
}