package edu.poly.datn_sd52_fa25_huynq203.library.exception.base;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessException extends RuntimeException {

    final ExceptionType exType;

    public BusinessException(ExceptionType exType) {
        super(exType.getDefaultMessage()); // BusinessException handled: Product not found with ID: 9007199254740991
        this.exType = exType;
    }

    public BusinessException(ExceptionType exType, String customerMessage) {
        super(customerMessage);
        this.exType = exType;
    }
}
