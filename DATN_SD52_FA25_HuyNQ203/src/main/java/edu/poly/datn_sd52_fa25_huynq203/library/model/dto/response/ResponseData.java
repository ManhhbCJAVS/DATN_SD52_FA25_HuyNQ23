package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ResponseData<T> {
    int status;
    String message;
    T data;

}
