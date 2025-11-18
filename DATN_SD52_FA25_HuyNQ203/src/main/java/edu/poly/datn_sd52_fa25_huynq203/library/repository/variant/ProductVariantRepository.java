package edu.poly.datn_sd52_fa25_huynq203.library.repository.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>, JpaSpecificationExecutor<ProductVariant> {

    /**
     * Tìm kiếm các biến thể dựa trên Product ID và danh sách các Color ID.
     * Dùng để kiểm tra sự tồn tại của tổ hợp Product-Color.
     */
    List<ProductVariant> findByProduct_IdAndColor_IdIn(Long productId, List<Long> colorIds);

}
