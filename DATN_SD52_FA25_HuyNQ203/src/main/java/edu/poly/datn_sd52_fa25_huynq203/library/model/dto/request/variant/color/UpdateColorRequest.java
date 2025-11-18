package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant.color;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.UpdateAttributeRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateColorRequest extends UpdateAttributeRequest {
    private String hexCode;
}