package edu.poly.datn_sd52_fa25_huynq203.library.repository.discount;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount.DiscountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DiscountDetailRepository extends JpaRepository<DiscountDetail, Long> {

    @Query("""
        SELECT dd FROM DiscountDetail dd
        WHERE dd.productVariant.id = :variantId
          AND dd.discount.status = 'ACTIVE'
          AND :now BETWEEN dd.discount.startDate AND dd.discount.endDate
    """)
    List<DiscountDetail> findActiveDiscountsForVariant(@Param("variantId") Long variantId,
                                                       @Param("now") LocalDateTime now);



    List<DiscountDetail> findByDiscountId(Long id);

}
