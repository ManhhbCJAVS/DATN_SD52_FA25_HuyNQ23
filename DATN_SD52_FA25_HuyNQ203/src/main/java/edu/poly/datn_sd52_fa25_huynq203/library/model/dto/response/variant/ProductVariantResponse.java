package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductVariantResponse {
    Long id;
    String code;
    String qrCode;
    //
    Long price;
    Integer quantity;
    String description;
    //
    String status;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdAt;
}
