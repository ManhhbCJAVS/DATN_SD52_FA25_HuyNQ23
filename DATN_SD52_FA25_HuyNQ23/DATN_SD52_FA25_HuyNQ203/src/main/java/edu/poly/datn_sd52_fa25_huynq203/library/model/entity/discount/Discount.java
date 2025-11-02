//package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.discount;
//
//import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.DiscountStatus;
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
//@Table(name = "discount")
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@Builder
//public class Discount {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long id;
//
//    Integer discountPercentage;
//    LocalDateTime startDate;
//    LocalDateTime endDate;
//
//    DiscountStatus status;
//    LocalDateTime createdAt;
//    LocalDateTime updatedAt;
//
//}
