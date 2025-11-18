package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute.Brand;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.ProductVariant;
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
import jakarta.persistence.OneToMany;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)

@ToString //(onlyExplicitlyIncluded = true)
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "code", unique = true)
    @Builder.Default
    String code = "PRD-" + java.util.UUID.randomUUID().toString();

    @Column(name = "name", nullable = false, length = 200)
    String name;



    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    ProductStatus status = ProductStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_brand", nullable = false)
    Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    //@ToString.Exclude
    List<ProductVariant> variants = new ArrayList<>(); // Khởi tạo để tránh NullPointerException;
}
