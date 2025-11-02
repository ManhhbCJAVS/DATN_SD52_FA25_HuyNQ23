//package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon;
//
//import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CouponStatus;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.experimental.FieldDefaults;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "coupon")
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Builder
//public class Coupon {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//    @Column(name="code", nullable = false, unique = true)
//    String code;
//    String name;
//    //
//    String discountType;
//    Integer discountValue;
//    Integer minimumCondition;
//    //
//    int couponCount;
//    //
//    LocalDateTime startDate;
//    LocalDateTime endDate;
//    //
//    LocalDateTime createdAt;
//    CouponStatus status;
//
//}
