package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.variant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse extends ImageUploadResponse {
    Long id;
    Long variantId;
}
