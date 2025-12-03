package edu.poly.datn_sd52_fa25_huynq203.library.service.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.CreateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.PageResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.CommonAttribute;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.attribute.BaseAttributeMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.CommonAttributeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

// "bản thiết kế" (template) | "KHUNG LOGIC" chung: Xử lý tất cả logic chung.
// Lớp thực hiện (implements) "HỢP ĐỒNG"
@NoRepositoryBean
@Slf4j

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public abstract class AbstractBaseAttributeService<
        E extends CommonAttribute,
        ID extends Serializable, // <--- CÓ CONSTRAINT ID EXTENDS Serializable
        CR extends CreateAttributeRequest,
        UR extends UpdateAttributeRequest,
        R extends CommonAttributeResponse,
        M extends BaseAttributeMapper<E, CR, UR, R>
        > {
    SpecificationBuilderService specificationBuilderService; //Inject By Spring IoC Container

    // Abstract methods: Công cụ lớp con phải cung cấp: để xử lý logic.
    protected abstract CommonAttributeRepository<E, ID> getCommonAttributeRepository();


    protected abstract M getMapper();

    // Logic Tùy biến (Template Method Pattern)
    protected void handleCustomCreateLogic(E entity, CR request) { /* Mặc định không làm gì */ }

    protected void handleCustomUpdateLogic(E entity, UR request) { /* Mặc định không làm gì */ }

    // Logic CREATE chung: Có kiểm tra trùng tên

    @Transactional(rollbackFor = Exception.class)
    public R create(CR request) {

        getCommonAttributeRepository().findByName(request.getName()).ifPresent(attribute -> {
            throw new BusinessException(ExceptionType.DUPLICATE_FIELD_VALUE, "Thuộc tính " + request.getName() + " đã tồn tại.");
        });

        E entity = getMapper().mapCreateRequestToEntity(request);
        log.info("Call method: create with params entity: {}", entity);

        handleCustomCreateLogic(entity, request);

        entity = getCommonAttributeRepository().save(entity);
        return getMapper().mapEntityToResponse(getCommonAttributeRepository().save(entity));
    }

    // Logic UPDATE (PATCH) chung: Có kiểm tra trùng tên và trạng thái isDeleted

    @Transactional(rollbackFor = Exception.class)
    public R update(ID id, UR request) {
        E entity = getCommonAttributeRepository().findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "Thuộc tính (Id): " + id + " không tồn tại."));

        // Logic kiểm tra trùng trạng thái isDeleted
        if (request.getIsDeleted() != null && request.getIsDeleted().equals(entity.getIsDeleted())) {
            throw new BusinessException(
                    ExceptionType.INVALID_REQUEST_BODY,
                    String.format("Thuộc tính đã ở trạng thái %s (ID: %s).",
                            entity.getIsDeleted() ? "XÓA MỀM" : "HOẠT ĐỘNG",
                            id
                    )
            );
        }

        // MapStruct/Mapper update field not null
        getMapper().mapUpdateRequestToEntity(request, entity);

        // *** Tùy biến: Thêm logic custom TẠI ĐÂY nếu cần ***
        handleCustomUpdateLogic(entity, request);

        return getMapper().mapEntityToResponse(getCommonAttributeRepository().save(entity));
    }

    // Logic FindBySpecifications (GENERALIZED)

    @Transactional(readOnly = true)
    public PageResponse findBySpecifications(boolean disablePaging, Pageable pageable, String[] filterParams) {
        boolean isPagingDisabled = Boolean.TRUE.equals(disablePaging);

        log.info("Call method: getBySpecifications with params filterParams: {} + isUnpaged: {} + Pageable: {}",
                filterParams, isPagingDisabled, pageable);

        // Tạo Specification
        Specification<E> spec = specificationBuilderService.buildSpecification(filterParams);

        if (isPagingDisabled) {
            List<E> entities = getCommonAttributeRepository().findAll(spec, pageable.getSort());
            Pageable fullPageable = PageRequest.of(0, entities.size() > 0 ? entities.size() : 1, pageable.getSort());
            return convertToPageResponse(new PageImpl<>(entities, fullPageable, entities.size()));
        } else {
            Page<E> entities = getCommonAttributeRepository().findAll(spec, pageable);
            return convertToPageResponse(entities);
        }
    }

    // Helper method: Chuyển đổi Page<E> sang PageResponse
    protected PageResponse convertToPageResponse(Page<E> page) {
        return PageResponse.builder()
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .pageNumber(page.getNumber())
                .content(getMapper().mapEntitiesToResponses(page.getContent()))
                .build();
    }
}