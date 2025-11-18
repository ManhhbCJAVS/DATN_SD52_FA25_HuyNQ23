package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CommonAttributeResponse {
    Long id;
    String code;
    String name;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime createdAt;

    Boolean isDeleted; // Luôn hiển thị trạng thái xóa mềm
}
