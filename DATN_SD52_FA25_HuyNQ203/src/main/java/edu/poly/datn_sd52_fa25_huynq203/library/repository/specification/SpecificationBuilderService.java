package edu.poly.datn_sd52_fa25_huynq203.library.repository.specification;

import edu.poly.datn_sd52_fa25_huynq203.library.untils.AppConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@Slf4j
public class SpecificationBuilderService {
    private final Pattern pattern = Pattern.compile(AppConst.SEARCH_SPEC_OPERATOR);
    /**
     * Xây dựng Specification từ một mảng các chuỗi điều kiện tìm kiếm.
     *
     * @param filterParams Mảng các điều kiện tìm kiếm (e.g., ["name:John", "age>30"])
     * @param <T>          Loại Entity mà Specification áp dụng
     * @return Specification<T> đã được xây dựng, hoặc null nếu filterParams rỗng
     */
    public <T> Specification<T> buildSpecification(String[] filterParams) {
        if (filterParams == null || filterParams.length == 0) return null;

        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();

        for (String param : filterParams) {
            //param = "age>18"
            var matcher = pattern.matcher(param);
            if (matcher.find()) {
                String key = matcher.group(1);// Nhóm 1: key (ví dụ: "age")
                String operator = matcher.group(2);// Nhóm 2: operator (ví dụ: ">")
                String value = matcher.group(4);// Nhóm 4: value (ví dụ: "18")
                String prefix = matcher.group(3);// Nhóm 3: prefix (ví dụ: "!")
                String suffix = matcher.group(5);// Nhóm 5: suffix (ví dụ: "*")

                // Giả định cú pháp OR/AND nằm trong giá trị prefix/suffix hoặc được xử lý bởi một parser phức tạp hơn.
                // Để đơn giản hóa, chúng ta không dùng cờ orPredicate trong builder.with() này.
                // Nếu muốn hỗ trợ OR/AND, cần phải sửa đổi regex và logic.
                String trimmedValue = cleanSearchValue(value);
                log.info("Phân tích bộ lọc: [{}]. Key: [{}], Operator: [{}], Prefix: [{}], Value: [{}], Suffix: [{}]",
                        param, key, operator, prefix, trimmedValue, suffix);

                builder.with(key, operator, trimmedValue, prefix, suffix);
            }
        }

        // Cần xử lý trường hợp builder.build() trả về null nếu không có tham số nào hợp lệ.
        return builder.build();
    }

// --- HÀM TIỆN ÍCH MỚI ---
    /**
     * Thực hiện loại bỏ khoảng trắng dư thừa cho giá trị tìm kiếm,
     * tương tự như logic trong setter của bạn.
     *
     * @param value Chuỗi giá trị đầu vào (ví dụ: " Apple  MacBook  ")
     * @return Chuỗi đã được làm sạch (ví dụ: "Apple MacBook")
     */
    private String cleanSearchValue(String value) {
        if (value == null) {
            return ""; // Trả về chuỗi rỗng để được bỏ qua trong vòng lặp chính
        }

        // 1. Dùng trim() để loại bỏ khoảng trắng ở hai đầu
        // 2. Dùng replaceAll("\\s+", " ") để thay thế nhiều khoảng trắng thành một khoảng trắng
        // 3. (Tùy chọn) Gọi trim() lần nữa để xử lý trường hợp input chỉ toàn khoảng trắng
        return value.trim().replaceAll("\\s+", " ").trim();
    }

}
