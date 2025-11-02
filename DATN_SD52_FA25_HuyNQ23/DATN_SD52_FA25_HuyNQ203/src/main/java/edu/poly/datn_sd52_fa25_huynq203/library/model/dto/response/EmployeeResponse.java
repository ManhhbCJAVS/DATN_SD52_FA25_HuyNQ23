package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Gender;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String email;
    private Role role;
    private String avatar;
    private LocalDate birthday;
    private Gender gender;
    private String note;
    private EmployeeStatus status;
    private LocalDate createdAt;
}
