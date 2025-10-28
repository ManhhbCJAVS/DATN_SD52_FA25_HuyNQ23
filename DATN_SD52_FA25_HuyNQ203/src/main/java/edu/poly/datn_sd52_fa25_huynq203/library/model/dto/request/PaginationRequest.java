package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request;


import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PaginationRequest {
    Integer pageNumber;
    Integer pageSize;
}
