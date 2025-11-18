package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.CommonAttributeResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColorResponse extends CommonAttributeResponse {
    // Thêm trường đặc thù của Color
    private String hexCode;
}
