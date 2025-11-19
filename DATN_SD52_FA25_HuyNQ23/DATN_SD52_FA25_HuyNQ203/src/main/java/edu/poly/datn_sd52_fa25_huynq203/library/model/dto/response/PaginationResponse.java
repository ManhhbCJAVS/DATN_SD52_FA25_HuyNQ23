package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Setter
public class PaginationResponse {
    long totalElements;
    int pageSize;
    int totalPages;
    int pageNumber;
    Object content;
}
