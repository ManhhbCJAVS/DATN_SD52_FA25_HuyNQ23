package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.CreateVariantRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.UpdateVariantRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.PageResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ProductVariantResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.Product;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Color;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.variant.ProductVariantMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.ProductRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.variant.ProductVariantRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.variant.attribute.ColorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductVariantService {
    ProductVariantRepository productVariantRepository;
    ProductRepository productRepository;
    ColorRepository colorRepository;
    SpecificationBuilderService specificationBuilderService;
    ProductVariantMapper productVariantMapper;

    /**
     * Cập nhật thông tin cho một hoặc nhiều biến thể sản phẩm (Patch Semantic).
     * Chỉ cập nhật các trường được gửi về trong Request Body.
     * * @param updateVariantRequests Danh sách các DTO chứa variantId và các trường cập nhật.
     *
     * @return Danh sách các biến thể đã được cập nhật.
     */
    @Transactional(rollbackFor = Exception.class)
    public List<ProductVariantResponse> updateVariantRequests(List<UpdateVariantRequest> updateVariantRequests) {
        log.info("Updating Product Variants with Request Count: {}", updateVariantRequests.size());

        // 1. Thu thập tất cả Variant IDs cần cập nhật
        Set<Long> requestedVariantIds = updateVariantRequests.stream()
                .map(UpdateVariantRequest::getVariantId)
                .collect(Collectors.toSet());

        // 2. Truy vấn tất cả Variants hiện có trong DB (chỉ update những cái tồn tại)
        List<ProductVariant> existingVariants = productVariantRepository.findAllById(requestedVariantIds);


        // Chuyểnn DTOs cập nhật ->  Map (Key: Variant ID) : tìm kiếm
        // 101	{ variantId: 101, price: 500, quantity: 10 }
        // 205	{ variantId: 205, description: "New Dsc" }
        // 33	{ variantId: 33, price: 150 }
        Map<Long, UpdateVariantRequest> requestMap = updateVariantRequests.stream()
                .collect(Collectors.toMap(
                        UpdateVariantRequest::getVariantId, // <--- KEY (Chìa khóa tra cứu)
                        Function.identity()) // <--- VALUE (Giá trị lưu trữ)
                );

        // 4. Áp dụng các thay đổi Patch Semantic
        List<ProductVariant> variantsToSave = existingVariants.stream()
                .map(variant -> {
                    UpdateVariantRequest dto = requestMap.get(variant.getId());
                    // Sử dụng MapStruct với logic IGNORE_NULL để chỉ cập nhật các trường không null
                    productVariantMapper.updateVariantFromDto(dto, variant);
                    return variant;
                })
                .collect(Collectors.toList());

        // 5. Lưu trữ
        List<ProductVariant> savedVariants = productVariantRepository.saveAll(variantsToSave);

        log.info("Successfully updated {} variants.", savedVariants.size());
        return productVariantMapper.toListResponseBasic(savedVariants);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ProductVariantResponse> createVariantRequest(Long productId, List<CreateVariantRequest> createVariantRequests) {
        log.info("Creating Product Variant with Request: {}", createVariantRequests);
        Product productSaved = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

        // Kiểm tra trùng lặp với CSDL
        validateDuplicateVariants(productId, createVariantRequests);

        List<ProductVariant> newVariants = createVariantRequests.stream()
                .map(variantRequest -> mapAndSetAttributes(productSaved, variantRequest))
                .collect(Collectors.toList());

        log.info("Mapped Product Variant newEntities: {}", newVariants);
        return productVariantMapper.toListResponseBasic(productVariantRepository.saveAll(newVariants));
    }

    /**
     * Kiểm tra xem các biến thể dựa trên tổ hợp Product ID và Color ID đã tồn tại chưa.
     * Nếu tồn tại, ném ra BusinessRuleException.
     */
    private void validateDuplicateVariants(Long productId, List<CreateVariantRequest> createVariantRequests) {
        // Thu thập tất cả các Color ID được yêu cầu
        List<Long> requestedColorIds = createVariantRequests.stream()
                .map(CreateVariantRequest::getColorId)
                .collect(Collectors.toList());

        // Thực hiện truy vấn DB MỘT LẦN: return variants đã tồn tại vs productId & colorId.
        // SELECT * FROM product_variant pv WHERE pv.product_id = 42 AND pv.color_id IN (101, 105, 112);
        List<ProductVariant> existingVariants = productVariantRepository.findByProduct_IdAndColor_IdIn(productId, requestedColorIds);

        if (!existingVariants.isEmpty()) {
            String duplicateIds = existingVariants.stream()
                    .map(variant -> variant.getColor().getId()) // Lấy ID màu Long
                    .distinct() // Lọc các ID trùng lặp
                    .map(String::valueOf) // Chuyển →  String
                    .collect(Collectors.joining(", ")); // Nối thành chuỗi "101, 112"

            throw new RuntimeException(
                    String.format("Variant(s) already exist for Product ID %d with Color ID(s): %s",
                            productId, duplicateIds)
            );
        }
    }

    private ProductVariant mapAndSetAttributes(Product savedProduct, CreateVariantRequest variantRequest) {
        Color color = colorRepository.findById(variantRequest.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found with ID: " + variantRequest.getColorId()));

        ProductVariant variant = productVariantMapper.convertRequestToEntity(variantRequest);

        variant.setColor(color);
        variant.setProduct(savedProduct);
        return variant;
    }


    @Transactional(readOnly = true)
    public PageResponse findBySpecifications(boolean disablePaging, Pageable pageable, String[] filterParams) {
        boolean isPagingDisabled = Boolean.TRUE.equals(disablePaging);

        log.info("Call method: getBySpecifications with params filterParams: {} + isUnpaged: {} + Pageable: {}",
                filterParams, isPagingDisabled, pageable);

        Specification<ProductVariant> variantSpec = specificationBuilderService.buildSpecification(filterParams);

        if (isPagingDisabled) {//ko phân trang (Lấy TẤT CẢ)
            List<ProductVariant> variants = productVariantRepository.findAll(variantSpec);
            Pageable fullPageable = PageRequest.of(
                    0,
                    variants.size() > 0 ? variants.size() : 1,
                    pageable.getSort()
            );
            return convertToPageResponse(new PageImpl<>(variants, fullPageable, variants.size()));

        } else {
            Page<ProductVariant> variants = productVariantRepository.findAll(variantSpec, pageable);
            return convertToPageResponse(variants);
        }
    }

    private PageResponse convertToPageResponse(Page<ProductVariant> variants) {
        return PageResponse.builder().
                totalElements(variants.getTotalElements()).
                pageSize(variants.getSize()).
                totalPages(variants.getTotalPages()).
                pageNumber(variants.getNumber()).
                content(productVariantMapper.toListResponseBasic(variants.getContent())).
                build();
    }

//    @Transactional(readOnly = true)
//    public List<ProductVariantResponse> findAllProductVariants() {
//        List<ProductVariant> variants = productVariantRepository.findAll();
//        log.info("Found Variants: {}", variants);
//        return productVariantMapper.toListResponseBasic(variants);
//    }


    // BỔ SUNG: 1. Kiểm tra trùng lặp ID biến thể ngay trong Request Body
    //[
    //  { "variantId": 1, "price": 2439, ... }, // Lần xuất hiện thứ nhất của ID 1
    //  { "variantId": 1, "price": 39, ... }    // Lần xuất hiện thứ hai của ID 1 (Gây lỗi)
    //]
    //validateInternalDuplicatesForUpdate(updateVariantRequests);
//    private void validateInternalDuplicatesForUpdate(List<UpdateVariantRequest> updateVariantRequests) {
//        // 1. Tìm các Variant ID bị trùng lặp
//        Set<Long> duplicateVariantIds = updateVariantRequests.stream()
//                .map(UpdateVariantRequest::getVariantId)
//                .collect(Collectors.groupingBy(
//                        Function.identity(),
//                        Collectors.counting() // Đếm số lần xuất hiện
//                ))
//                .entrySet().stream()
//                .filter(entry -> entry.getValue() > 1) // Lọc ra các ID có Count > 1
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toSet());
//
//        // 2. Xử lý Exception nếu có trùng lặp
//        if (!duplicateVariantIds.isEmpty()) {
//            String duplicateIds = duplicateVariantIds.stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.joining(", "));
//
//            throw new RuntimeException(
//                    String.format("Duplicate Variant ID(s) found in the request body: %s. A batch update request cannot contain multiple updates for the same Variant ID.",
//                            duplicateIds)
//            );
//        }
//    }
//    /**
//     * Kiểm tra xem có yêu cầu tạo Biến thể nào trùng lặp Color ID trong cùng một Request Body hay không.
//     * // BƯỚC MỚI: Kiểm tra trùng lặp nội bộ trong Request Body : (productId: 1, colorId: 2) & (productId: 1, colorId: 2)
//     *         validateInternalDuplicates(createVariantRequests); // <--- Bổ sung
//     */
    //    private void validateInternalDuplicates(List<CreateVariantRequest> createVariantRequests) {
    //        // 1. Tìm các Color ID bị trùng lặp
    //        Set<Long> duplicateColorIds = createVariantRequests.stream()
    //                .map(CreateVariantRequest::getColorId) // Ánh xạ danh sách Request thành luồng Color ID
    //                .collect(Collectors.groupingBy(
    //                        Function.identity(), // Gán Color ID làm Key
    //                        Collectors.counting() // Đếm số lần xuất hiện của mỗi Color ID
    //                )) // Kết quả là Map<Long (ColorId), Long (Count)>
    //                .entrySet().stream()
    //                .filter(entry -> entry.getValue() > 1) // Lọc ra những entry có Count > 1
    //                .map(Map.Entry::getKey) // Lấy lại Color ID (Key)
    //                .collect(Collectors.toSet()); // Thu thập các ID bị trùng lặp
    //
    //        // 2. Xử lý Exception nếu có trùng lặp
    //        if (!duplicateColorIds.isEmpty()) {
    //            String duplicateIds = duplicateColorIds.stream()
    //                    .map(String::valueOf)
    //                    .collect(Collectors.joining(", "));
    //
    //            throw new RuntimeException(
    //                    String.format("Duplicate Color ID(s) found in the request body: %s. A Product cannot have two identical variants (Product ID + Color ID).",
    //                            duplicateIds)
    //            );
    //        }
    //    }
}
