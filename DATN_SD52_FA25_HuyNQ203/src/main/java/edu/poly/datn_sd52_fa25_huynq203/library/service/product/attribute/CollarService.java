package edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Collar;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.CollarMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CollarRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

@Slf4j
public class CollarService extends AbstractBaseAttributeService<
        Collar, 
        Long, 
        CreateAttributeRequest,
        UpdateAttributeRequest,
        CommonAttributeResponse,
        CollarMapper 
        > {
    CollarRepository CollarRepository;
    CollarMapper collarMapper;

    @Autowired
    public CollarService(
            CollarRepository CollarRepository,
            CollarMapper collarMapper,
            SpecificationBuilderService specificationBuilderService
    ) {
        super(specificationBuilderService);
        this.CollarRepository = CollarRepository;
        this.collarMapper = collarMapper;
    }


    // [BẮT BUỘC] Trả về Repository cho lớp cha
    @Override
    protected CommonAttributeRepository<Collar, Long> getCommonAttributeRepository() { // 👈 SỬA KIỂU TRẢ VỀ
        return CollarRepository;
    }


    @Override
    protected CollarMapper getMapper() {
        return collarMapper;
    }
}
