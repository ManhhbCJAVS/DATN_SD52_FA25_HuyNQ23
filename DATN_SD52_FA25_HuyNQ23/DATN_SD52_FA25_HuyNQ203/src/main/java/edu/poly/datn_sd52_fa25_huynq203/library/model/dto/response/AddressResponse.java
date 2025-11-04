package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private String id;
    private String customerName;
    private String employeeName;
    private String provinceName;
    private String districtName;
    private String wardName;
    private boolean isDefault;
}
