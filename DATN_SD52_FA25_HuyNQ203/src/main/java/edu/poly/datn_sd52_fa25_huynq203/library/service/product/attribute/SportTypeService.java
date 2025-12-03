package edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.SportType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.SportTypeMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.SportTypeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SportTypeService extends AbstractBaseAttributeService<
        SportType, // E: Entity
        Long, // ID: ID Type
        CreateAttributeRequest, // CR: Create Request Type
        UpdateAttributeRequest, // UR: Update Request Type
        CommonAttributeResponse, // R: Response Type
        SportTypeMapper
        > {
    SportTypeRepository sportTypeRepository;
    SportTypeMapper sportTypeMapper;

    @Autowired
    public SportTypeService(
            SportTypeRepository sportTypeRepository,
            SportTypeMapper sportTypeMapper,
            SpecificationBuilderService specificationBuilderService
    ) {
        super(specificationBuilderService);
        this.sportTypeRepository = sportTypeRepository;
        this.sportTypeMapper = sportTypeMapper;
    }

    @Override
    protected CommonAttributeRepository<SportType, Long> getCommonAttributeRepository() { // 👈 SỬA KIỂU TRẢ VỀ
        return sportTypeRepository;
    }

    @Override
    protected SportTypeMapper getMapper() {
        return sportTypeMapper;
    }
}