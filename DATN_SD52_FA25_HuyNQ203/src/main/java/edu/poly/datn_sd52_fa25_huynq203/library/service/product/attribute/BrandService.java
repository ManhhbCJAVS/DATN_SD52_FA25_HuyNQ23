package edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Brand;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.BrandMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.BrandRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)

@Slf4j
public class BrandService extends AbstractBaseAttributeService<
        Brand, // E: Entity
        Long, // ID: ID Type
        CreateAttributeRequest, // CR: Create Request Type
        UpdateAttributeRequest, // UR: Update Request Type
        CommonAttributeResponse, // R: Response Type
        BrandMapper
        > {
    BrandRepository brandRepository;
    BrandMapper brandMapper;

    @Autowired //Spring IoC Container: Dựa vào Bean đc tạo tiêm phụ thuộc
    public BrandService(
            BrandRepository brandRepository,
            BrandMapper brandMapper,
            SpecificationBuilderService specificationBuilderService // <-- Thêm dependency của lớp cha
    ) {
        super(specificationBuilderService); // <-- GỌI CONSTRUCTOR CÓ THAM SỐ CỦA LỚP CHA
        this.brandRepository = brandRepository;
        this.brandMapper = brandMapper;
    }
// =================================================================
    // 1. TRIỂN KHAI ABSTRACT METHODS (Minimal Code) ⇔ Khai báo công cụ.
    // =================================================================

    // [BẮT BUỘC] Trả về Repository cho lớp cha
    @Override
    protected CommonAttributeRepository<Brand, Long> getCommonAttributeRepository() { // 👈 SỬA KIỂU TRẢ VỀ
        return brandRepository;
    }

    @Override
    protected BrandMapper getMapper() {
        return brandMapper;
    }

}
