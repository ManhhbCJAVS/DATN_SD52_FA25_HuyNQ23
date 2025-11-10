package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Gender;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "employee")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Employee {
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
    @Column(name = "permission")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'STAFF'")
    Role role;
    //
    @Column(name = "avatar")
    String avatar;
    @Column(name = "birthday")
    LocalDate birthday;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(name = "note")
    String note;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("ACTIVE")
    EmployeeStatus status;

    @Column(name="created_at")
    LocalDate createdAt;
}
