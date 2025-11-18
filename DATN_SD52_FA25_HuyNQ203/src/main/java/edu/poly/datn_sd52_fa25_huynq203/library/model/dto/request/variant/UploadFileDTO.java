package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.variant;

// DTO chứa thông tin file để upload lên Cloudinary
// Cause: Thread request đóng <=> Tomcat clear file
// => Cần file lưu để exe (Thread pool) thực thi
public record UploadFileDTO(
        String originalFilename,
        String contentType,
        byte[] content
) {}
