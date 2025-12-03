package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {


    private Long customerId;
    private Long employeeId;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private boolean isDefault;
}
