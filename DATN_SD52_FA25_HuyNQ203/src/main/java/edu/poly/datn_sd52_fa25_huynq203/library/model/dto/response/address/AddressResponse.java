package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.address;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private Long id;
    private String customerName;
    private String employeeName;
    private String provinceName;
    private String districtName;
    private String wardName;
    private boolean isDefault;
}

