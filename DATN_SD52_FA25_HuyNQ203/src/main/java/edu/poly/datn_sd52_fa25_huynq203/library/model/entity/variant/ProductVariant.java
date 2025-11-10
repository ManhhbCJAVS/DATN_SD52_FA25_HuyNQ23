package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount.DiscountDetail;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.Product;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Color;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.ProductStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "product_variant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@ToString
@Builder
public class ProductVariant {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    //TODO: auto-generation code for product (Default value)
    @Column(name = "code", unique = true, nullable = false)
    @ColumnDefault("'PR-VAR:-<UUID>'")
    String code;

    @Column(name = "qr_code")
    //TODO: Auto-generate QR code: Xđ type của QRcode? (Default value)
    String qrCode;
    //
    @Column(name = "quantity")
    Integer quantity;

    // giá chính của sản phẩm
    @Column(name = "price")
    Double price;

    // giá sau khi áp dụng khuyến mãi
    @Column(name = "final_price")
    Double finalPrice;
    //
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    ProductStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;
    //
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @ToString.Exclude
    Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_color")
    Color color;



    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DiscountDetail> discountDetails;

}
