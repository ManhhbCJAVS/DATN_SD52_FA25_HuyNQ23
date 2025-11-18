package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class UpdateAttributeRequest {
    // 1. Trường 'isDeleted' để thay đổi trạng thái xóa mềm (Boolean wrapper để có thể là null)
    @Schema(description = "Trạng thái xóa mềm", example = "true")
    Boolean isDeleted;
}
