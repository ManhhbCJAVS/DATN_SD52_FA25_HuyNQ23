package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.bill.Bill;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Customer;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CouponStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
//    @Column(name="Ma", nullable = false, unique = true)
//    String code;

    @Column(name = "coupon_name")
    String name;

    @Column(name = "discount_type")
    String discountType;

    @Column(name = "discount_condition")
    String discountValue;

    @Column(name = "discount_value")
    Integer minimumCondition;
    //
    @Column(name = "quantity")
    int couponCount;
    //
    @Column(name = "start_date")
    LocalDateTime startDate;

    @Column(name = "end_date")
    LocalDateTime endDate;
    //
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    CouponStatus status;

//    @ManyToOne
//    @JoinColumn(name = "id_khach_hang")
//    Customer customer;

//    @ManyToOne
//    @JoinColumn(name = "id_hoa_don")
//    Bill bill;
}

