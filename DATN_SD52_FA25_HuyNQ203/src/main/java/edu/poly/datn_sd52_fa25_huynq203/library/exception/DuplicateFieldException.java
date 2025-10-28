package edu.poly.datn_sd52_fa25_huynq203.library.exception;

public class DuplicateFieldException extends RuntimeException{
    private final String fieldName;
    private final String fieldValue;

    public DuplicateFieldException(String fieldName, String fieldValue) {
        super(String.format("Trường %s với giá trị %s đã tồn tại.", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
