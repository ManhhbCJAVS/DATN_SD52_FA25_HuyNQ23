package edu.poly.datn_sd52_fa25_huynq203.library.model.enums;

public enum CustomerStatus {
    ACTIVE("Đang hoạt động"),
    INACTIVE("Ngừng hoạt động");

    private final String displayName;

    CustomerStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
