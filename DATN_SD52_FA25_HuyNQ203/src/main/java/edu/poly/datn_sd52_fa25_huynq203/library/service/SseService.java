package edu.poly.datn_sd52_fa25_huynq203.library.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SseService {
    // 1. **KHO LƯU TRỮ CHỦ ĐỀ (TOPIC STORAGE)**
    // Dùng Map để nhóm các kết nối (emitters) theo từng Chủ đề/Kênh (topic).
    // * Key: Tên Chủ đề (Ví dụ: "thông báo_sản_phẩm", "tin_tức_chung", "thông báo_đơn_hàng", "Thông tin ảnh upload").
    // * Value: Danh sách các SseEmitter đang lắng nghe Chủ đề đó.
    // Mục đích: Cho phép BE gửi thông báo có chọn lọc theo kênh (Topic).
    Map<String, List<SseEmitter>> emittersByTopic = new ConcurrentHashMap<>();

    /**
     * 2. **ĐĂNG KÝ KẾT NỐI SSE (SERVER-SENT EVENTS)**
     *
     * Hàm này thiết lập một kết nối mới và đăng ký nó vào một chủ đề cụ thể.
     * Client (FE): cung cấp kênh ('topic'): để BE ở kênh đó gửi gửi evt (payload) lên mỗi khi có thay đổi.
     */
    public SseEmitter registerEmitter(String topic) {
        SseEmitter emitter = new SseEmitter(3600000L); // disconnect if in 1H (timeout) not receive any event
        // Thêm emitter vào topic tương ứng
        emittersByTopic  //Ktr topic tồn tại ? Trả về d.s hiện có (List<emitter>) : exe lambda (tạo d.s mới)
                .computeIfAbsent(topic, t -> new CopyOnWriteArrayList<>())
                .add(emitter); // Thêm vào cuối d.s (cũ/tạo mới).

        // 3. Handle Disconnection & Timeout
        // Khi 1 evt cụ thể xảy ra -> server (BE) nhận biết & tự động gọi & thực thi hàm gọi lại (callback) đã đăng ký.
        // evt: kết nối bị đóng (client đóng tab, timeout, BE đóng = emitter.complete()) -> remove emitter in list.
        emitter.onCompletion(() -> emittersByTopic.get(topic).remove(emitter));
        emitter.onTimeout(() -> {
            emitter.complete();
            emittersByTopic.get(topic).remove(emitter);
        });

        // Gửi sự kiện khởi tạo (optional)
        try {
            emitter.send(SseEmitter.event().
                    name("initial"). // tên evt
                    data("Connection established for topic: " + topic)); // payload của evt.
        } catch (IOException e) {
            emitter.complete();
            emittersByTopic.get(topic).remove(emitter);
        }
        return emitter;
    }

    /**
     * GỬI SỰ KIỆN DỮ LIỆU ĐẾN TẤT CẢ CLIENT ĐANG KẾT NỐI TRONG MỘT CHỦ ĐỀ (TOPIC) CỤ THỂ
     *
     * @param topic Tên Chủ đề/Kênh mà dữ liệu cần được gửi tới (Ví dụ: "thông báo_đơn_hàng").
     * @param data Dữ liệu (Payload) cần gửi, thường là một chuỗi JSON.
     */
    public void sendToTopic(String topic, String data) {
        List<SseEmitter> emitters = emittersByTopic.get(topic);
        if (emitters == null) return; // k có ai sub kênh này => Hủy gửi payload.

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("message").data(data));
            } catch (IOException e) {
                // Nếu client đã disconnect: Gọi complete() để kích hoạt callback onCompletion/onTimeout.
                // Callback này sẽ chịu trách nhiệm remove emitter khỏi list MỘT CÁCH AN TOÀN.
                emitter.complete();
                // 💡 LƯU Ý QUAN TRỌNG:
                // BỎ dòng emitters.remove(emitter) ở đây để tránh race condition
                // với container và logic onCompletion đã đăng ký.
                // Dòng sau bị xóa: emitters.remove(emitter);
            }
        }
    }

    /**
     * 3. (Optional) Gửi dữ liệu tới một client riêng biệt
     * Có thể dùng map clientId -> emitter nếu muốn unicast
     */
    public void sendToClient(SseEmitter emitter, String data) {
        try {
            emitter.send(SseEmitter.event().name("message").data(data));
        } catch (IOException e) {
            emitter.complete();
        }
    }
}
