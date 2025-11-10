package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.DiscountStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "discount")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(name = "code", unique = true)
    String code;

    Double discountPercentage; // phần % giảm

    LocalDateTime startDate;

    LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DiscountStatus status;

    boolean deleted;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "discount", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DiscountDetail> discountDetails;

}
