package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Builder
public class PageResponse {
    long totalElements;
    int pageSize;
    int totalPages;
    int pageNumber;
    Object content;
}
