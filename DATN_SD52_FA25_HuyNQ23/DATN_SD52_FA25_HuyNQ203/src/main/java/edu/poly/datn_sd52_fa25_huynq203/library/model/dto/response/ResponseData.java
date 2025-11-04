package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> {
    private boolean success;
    private String message;
    private T data;
}
