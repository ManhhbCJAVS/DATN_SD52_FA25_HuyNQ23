package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import java.io.Serializable;
import lombok.experimental.FieldDefaults;
@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ResponseData<T> implements Serializable {


    int status;
    String message;
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
//    private final Integer status; // log 200
//    private final String message; //
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private T data;
//
//    /**
//     * Response data for the API to retrieve data successfully. For GET, POST only
//     *
//     * @param status
//     * @param message
//     * @param data
//     */
//    public ResponseData(Integer status, String message, T data) {
//        this.status = status;
//        this.message = message;
//        this.data = data;
//    }
//
//    /**
//     * Response data when the API executes successfully or getting error. For PUT, PATCH, DELETE
//     *
//     * @param status
//     * @param message
//     */
//    public ResponseData(Integer status, String message) {
//        this.status = status;
//        this.message = message;
//    }

}
