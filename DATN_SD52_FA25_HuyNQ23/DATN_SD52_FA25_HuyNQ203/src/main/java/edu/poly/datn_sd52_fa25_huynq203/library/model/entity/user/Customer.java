package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user;


import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CustomerStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    //
    @Column(name="code", nullable = false, unique = true)
    String code;
    @Column(name="name")
    String name;
    //
    @Column(name="phone")
    String phone;
    @Column(name="email")
    String email;
    @Column(name = "password")
    String password;
    //
    @Column(name = "avatar")
    String avatar;
    @Column(name = "birthday", nullable = true)
    LocalDate birthday;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    Gender gender;

    //
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("ACTIVE")
    CustomerStatus status;

    @Column(name="created_at")
    LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    Employee createdBy;

    @Column(name="updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    Employee updatedBy;
}
