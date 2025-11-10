package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount;


import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
        name = "discount_detail",
        uniqueConstraints = @UniqueConstraint(columnNames = {"discount_id", "product_variant_id"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DiscountDetail {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    Discount discount;

    boolean deleted;

}
