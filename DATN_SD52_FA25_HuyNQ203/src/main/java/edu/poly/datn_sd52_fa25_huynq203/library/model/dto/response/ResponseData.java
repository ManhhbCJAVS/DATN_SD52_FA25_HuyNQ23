package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

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
