package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.bill;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.coupon.Coupon;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Customer;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;

@Entity
@Table(name = "bill")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    Integer code;

    String qr_code;

    String order_type;

    String recipient_name;

    Integer recipient_phone;

    String recipient_email;

    String delivery_address;

    BigDecimal shipping_fee;

    Date estimated_delivery_time;

    String note;

    @ManyToOne
    @JoinColumn(name = "id_customer")
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "id_coupon")
    Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    Employee employee;
}
