package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)

@AllArgsConstructor

public class PageParam {
    Integer pageNumber;
    Integer pageSize;
    String sortBy; // asc or desc

}
