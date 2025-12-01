package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Gender;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "Tên nhân viên không được để trống")
    private String name;

    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có 10 chữ số")
    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String password;

    private Role role;

    private String avatar;

    private LocalDate birthday;

    private Gender gender;

    private String note;

    private EmployeeStatus status;
}

