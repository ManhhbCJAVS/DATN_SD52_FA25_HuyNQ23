package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.product;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.ProductRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.UpdateProductRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.CreateVariantRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.PageResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.product.ProductResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ProductVariantResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.Product;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Brand;
import edu.poly.datn_sd52_fa25_huynq203.library.model.mapper.product.ProductMapper;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.ProductRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute.BrandRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.specification.SpecificationBuilderService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.variant.ProductVariantService;
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

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
//@Transactional //default readOnly = true
public class ProductService {
    ProductRepository productRepository;
    BrandRepository brandRepository;
    ProductMapper productMapper;

    ProductVariantService productVariantService;
    SpecificationBuilderService specificationBuilderService;

    @Transactional(rollbackFor = Exception.class)
    public ProductResponse updateProductRequest(UpdateProductRequest updateProductRequest) {
        Product product = productMapper.convertUpdateRequestToEntity(updateProductRequest);
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "Product not found with ID: " + product.getId()));
        existingProduct.setStatus(product.getStatus());
        return productMapper.toResponseBasic(existingProduct);
    }

    @Transactional(rollbackFor = Exception.class) //default readOnly = false
    public ProductResponse createProductRequest(ProductRequest productRequest, List<CreateVariantRequest> variantsRequest) {
        // Ktr business logic -> lưu csdl
        log.info("Creating Product with Request: {}", productRequest);
        Product newProduct = MapProductRequestAndSetAttributes(productRequest);

        Product productSaved = productRepository.save(newProduct);
        ProductResponse productResponse = productMapper.toResponseBasic(productSaved);

        List<ProductVariantResponse> variantResponses = //Ủy quyền
                productVariantService.createVariantRequest(productSaved.getId(), variantsRequest);

        productResponse.setVariantResponses(variantResponses);
        return productResponse;
    }

    private Product MapProductRequestAndSetAttributes(ProductRequest productRequest) {
        //Mapper ProductRequest
        Product product = productMapper.convertRequestToEntity(productRequest);
        //Set attributes
        Brand brand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(() -> new RuntimeException("Brand not found with ID: " + productRequest.getBrandId()));
        product.setBrand(brand);
        return product;
    }

    @Transactional(readOnly = true)
    public PageResponse findBySpecifications(boolean disablePaging, Pageable pageable, String[] filterParams) {
        boolean isPagingDisabled = Boolean.TRUE.equals(disablePaging);

        log.info("Call method: getBySpecifications with params filterParams: {} + isUnpaged: {} + Pageable: {}",
                filterParams, isPagingDisabled, pageable);

        Specification<Product> productSpec = specificationBuilderService.buildSpecification(filterParams);

        if (isPagingDisabled) {//ko phân trang (Lấy TẤT CẢ)
            // Sử dụng findAll(Specification, Sort) để lấy List<Product> và áp dụng sắp xếp
            List<Product> products = productRepository.findAll(productSpec, pageable.getSort());

            Pageable fullPageable = PageRequest.of(
                    0, // pageNumber: Luôn là 0
                    products.size() > 0 ? products.size() : 1, // pageSize: Bằng tổng số phần tử. Tránh lỗi chia 0
                    pageable.getSort()     // Sort: Giữ nguyên
            );
            return convertToPageResponse(new PageImpl<>(products, fullPageable, products.size()));

        } else { // CASE 2: có phân trang
            Page<Product> products = productRepository.findAll(productSpec, pageable);
            return convertToPageResponse(products);
        }
    }

    private PageResponse convertToPageResponse(Page<Product> products) {
        return PageResponse.builder().
                totalElements(products.getTotalElements()).
                pageSize(products.getSize()).
                totalPages(products.getTotalPages()).
                pageNumber(products.getNumber()).
                content(productMapper.toListResponseBasic(products.getContent())).
                build();
    }
}