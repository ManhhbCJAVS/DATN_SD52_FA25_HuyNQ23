package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant.ProductVariantResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    Long id;
    String code;
    String name;
    //
    String status;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdAt;

    List<ProductVariantResponse> variantResponses;
}
