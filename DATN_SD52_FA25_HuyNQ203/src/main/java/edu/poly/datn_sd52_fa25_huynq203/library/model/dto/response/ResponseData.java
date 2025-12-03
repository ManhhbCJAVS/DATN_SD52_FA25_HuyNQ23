package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@Builder
public class ResponseData<T> implements Serializable {
    int status; // log 200
    String message; //
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T data;

    //PUT PATCH DELETE
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    //GET POST
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
