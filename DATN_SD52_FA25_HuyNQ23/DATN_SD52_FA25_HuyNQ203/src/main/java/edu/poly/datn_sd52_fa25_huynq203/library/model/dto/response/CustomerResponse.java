package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CustomerStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private LocalDate birthday;
    private Gender gender;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdByName;
    private String updatedByName;
}
