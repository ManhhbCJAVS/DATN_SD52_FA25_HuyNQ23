package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.Product;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute.Color;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.ProductStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
    @Column(name = "code", unique = true)
    @Builder.Default
    String code = "PR-VAR-" + java.util.UUID.randomUUID().toString();

    @Column(name = "qr_code")
    //TODO: Auto-generate QR code: Xđ type của QRcode? (Default value)
    String qrCode;
    //
    @Column(name = "quantity")
    Integer quantity;
    @Column(name = "price")
    Long price;

    @Column(name = "description")
    String description;
    //
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    ProductStatus status = ProductStatus.ACTIVE;

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

}
