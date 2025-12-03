package edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Material;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.MaterialMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.MaterialRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MaterialService extends AbstractBaseAttributeService<
        Material, // E: Entity
        Long, // ID: ID Type
        CreateAttributeRequest, // CR: Create Request Type
        UpdateAttributeRequest, // UR: Update Request Type
        CommonAttributeResponse, // R: Response Type
        MaterialMapper
        > {
    MaterialRepository materialRepository;
    MaterialMapper materialMapper;

    @Autowired
    public MaterialService(
            MaterialRepository materialRepository,
            MaterialMapper materialMapper,
            SpecificationBuilderService specificationBuilderService
    ) {
        super(specificationBuilderService);
        this.materialRepository = materialRepository;
        this.materialMapper = materialMapper;
    }

    @Override
    protected CommonAttributeRepository<Material, Long> getCommonAttributeRepository() { // 👈 SỬA KIỂU TRẢ VỀ
        return materialRepository;
    }

    @Override
    protected MaterialMapper getMapper() {
        return materialMapper;
    }
}