//package edu.poly.datn_sd52_fa25_huynq203.library.config;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import lombok.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CloudinaryConfig {
//    @Value("${cloudinary.cloud-name}")
//    private String cloudName;
//
//    @Value("${cloudinary.api-key}")
//    private String apiKey;
//
//    @Value("${cloudinary.api-secret}")
//    private String apiSecret;
//
//    @Bean
//    public Cloudinary cloudinaryInstance() {
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", cloudName,
//                "api_key", apiKey,
//                "api_secret", apiSecret
//        ));
//    }
//}
