package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.MidSole;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.MidSoleMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.MidSoleRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MidSoleService extends AbstractBaseAttributeService<
        MidSole, // E: Entity
        Long, // ID: ID Type
        CreateAttributeRequest, // CR: Create Request Type
        UpdateAttributeRequest, // UR: Update Request Type
        CommonAttributeResponse, // R: Response Type
        MidSoleMapper
        > {
    MidSoleRepository midSoleRepository;
    MidSoleMapper midSoleMapper;

    @Autowired
    public MidSoleService(
            MidSoleRepository midSoleRepository,
            MidSoleMapper midSoleMapper,
            SpecificationBuilderService specificationBuilderService
    ) {
        super(specificationBuilderService);
        this.midSoleRepository = midSoleRepository;
        this.midSoleMapper = midSoleMapper;
    }

    @Override
    protected CommonAttributeRepository<MidSole, Long> getCommonAttributeRepository() { // 👈 SỬA KIỂU TRẢ VỀ
        return midSoleRepository;
    }


    @Override
    protected MidSoleMapper getMapper() {
        return midSoleMapper;
    }
}