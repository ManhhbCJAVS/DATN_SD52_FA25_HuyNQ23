package edu.poly.datn_sd52_fa25_huynq203.admin;

import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.SseService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor

@Slf4j
public class SseController {

    SseService sseService;

    @GetMapping("/subscribe/{topic}")
    public SseEmitter subscribe(@PathVariable String topic) {
        return sseService.registerEmitter(topic);
    }

    /**
     * 3. Endpoint gửi sự kiện tới client theo topic
     * @param topic:     topic muốn gửi
     * @param eventData: dữ liệu gửi tới FE
     */
    @PostMapping("/publish/{topic}")
    public void publishEvent(@PathVariable String topic, @RequestBody String eventData) {
        sseService.sendToTopic(topic, eventData);
    }
}
//import React, { useEffect, useState } => 'react';
//
//function SseComponent() {
//  const [message, setMessage] = useState('Chưa có kết nối...');
//
//    useEffect(() => {
//            // 1. Tạo kết nối EventSource tới endpoint subscribe của Spring Boot
//    const eventSource = new EventSource('http://localhost:8080/api/sse/subscribe');
//    // Lưu ý: Đảm bảo địa chỉ và port chính xác
//
//    // 2. Lắng nghe sự kiện mặc định (không có tên - event: data) hoặc sự kiện 'message'
//    eventSource.onmessage = (event) => {
//            console.log('Sự kiện không tên:', event.data);
//    setMessage(`[Event mặc định]: ${event.data}`);
//    };
//
//    // 3. Lắng nghe sự kiện tùy chỉnh (Ví dụ: sự kiện tên là 'message' từ Server)
//    eventSource.addEventListener('message', (event) => {
//            console.log('Sự kiện "message":', event.data);
//    // Giả sử event.data là một JSON string
//    setMessage(`[Event 'message']: ${event.data}`);
//    });
//
//    // 4. Lắng nghe sự kiện khởi tạo (Nếu bạn có gửi SseEmitter.event().name("initial") )
//    eventSource.addEventListener('initial', (event) => {
//            console.log('Sự kiện "initial":', event.data);
//    // Xử lý dữ liệu khởi tạo
//    });
//
//
//    // 5. Xử lý khi kết nối bị đóng hoặc lỗi
//    eventSource.onerror = (error) => {
//            console.error('EventSource lỗi:', error);
//    eventSource.close(); // Đóng kết nối nếu gặp lỗi nghiêm trọng
//    };
//
//    // Cleanup function: Đóng kết nối khi component bị unmount
//    return () => {
//            console.log('Đóng kết nối SSE.');
//    eventSource.close();
//    };
//  }, []); // [] đảm bảo useEffect chỉ chạy 1 lần khi component mount
//
//    return (
//            <div>
//            <h2>📡 Kết nối SSE với Server</h2>
//            <p>Thông báo mới nhất: <strong>{message}</strong></p>
//            </div>
//  );
//}
//export default SseComponent;
