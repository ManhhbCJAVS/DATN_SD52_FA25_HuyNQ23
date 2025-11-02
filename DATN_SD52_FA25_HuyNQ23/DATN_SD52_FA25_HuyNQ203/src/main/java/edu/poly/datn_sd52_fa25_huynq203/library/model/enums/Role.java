package edu.poly.datn_sd52_fa25_huynq203.library.model.enums;

public enum Role {
    ADMIN("Quản trị viên"),
    STAFF("Nhân viên"),
    MANAGER("Quản lý"); // Thêm dòng này

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}