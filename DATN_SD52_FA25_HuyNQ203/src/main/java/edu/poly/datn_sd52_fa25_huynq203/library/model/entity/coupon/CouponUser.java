package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupon")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "id_coupon")
    Coupon coupon;
}
