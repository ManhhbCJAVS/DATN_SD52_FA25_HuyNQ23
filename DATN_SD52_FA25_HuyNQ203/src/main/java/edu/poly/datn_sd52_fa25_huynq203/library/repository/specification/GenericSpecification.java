package edu.poly.datn_sd52_fa25_huynq203.library.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class GenericSpecification<T> implements Specification<T> {
    //ĐN:  Cách biến một điều kiện thành Predicate (điều kiện SQL).
    private SpecSearchCriteria criteria;

    /**
     * @variable path: Đại diện cho trường Entity ( VD: Product.name | Product.price )
     * @Method castToRequiredType(...) : Đại diện cho value tìm kiếm (sau khi chuyển từ String) (VD:  "Laptop", 100.0, 5)
     * WHERE name LIKE '%Pro%' (LIKE)
     * WHERE quantity = 10 (EQUALITY)
     * WHERE name = 'Phone X' (EQUALITY)
     * WHERE quantity != 0 (NEGATION)
     * WHERE price > 500.0 (GREATER_THAN)
     * WHERE price < 200.0 (LESS_THAN)
     */
    @Override

    public Predicate toPredicate(@NonNull final Root<T> root, @NonNull final CriteriaQuery<?> query, @NonNull final CriteriaBuilder builder) {
        // 'path' bây giờ trỏ đến thuộc tính cuối cùng (ví dụ: "brand.id" -> path trỏ đến "id")
        Path<?> path = getPath(root, criteria.getKey());
        Class<?> fieldType = path.getJavaType();
        Object convertedValue;

        try {
            // Thực hiện chuyển đổi kiểu dữ liệu trước
            convertedValue = castToRequiredType(fieldType, criteria.getValue());
        } catch (IllegalArgumentException e) {
            System.err.println("Lỗi chuyển đổi giá trị tìm kiếm cho trường " + criteria.getKey() + ": " + e.getMessage());
            return builder.disjunction(); // Trả về Predicate luôn sai (luôn trả về 0 kết quả)
        }

        return switch (criteria.getOperation()) {
            // --- String Operations (LIKE, STARTS_WITH, ENDS_WITH) ---
            case LIKE, CONTAINS, STARTS_WITH, ENDS_WITH -> {
                if (!String.class.isAssignableFrom(fieldType)) {
                    // Trả về Predicate luôn sai nếu LIKE được gọi trên trường không phải String
                    System.err.println("Lỗi: Phép toán LIKE chỉ hỗ trợ cho kiểu String. Trường: " + criteria.getKey());
                    yield builder.disjunction();
                }

                String stringValue = criteria.getValue().toString();
                yield switch (criteria.getOperation()) {
                    case STARTS_WITH -> builder.like((Path<String>) path, stringValue + "%");
                    case ENDS_WITH -> builder.like((Path<String>) path, "%" + stringValue);
                    default -> builder.like((Path<String>) path, "%" + stringValue + "%"); // LIKE | CONTAINS
                };
            }
            // --- Equality/Inequality Operations ---
            case EQUALITY -> builder.equal(path, convertedValue);
            case NEGATION -> builder.notEqual(path, convertedValue);

            // --- Comparison Operations (GREATER/LESS THAN) ---
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), (Comparable) convertedValue);
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), (Comparable) convertedValue);
            default -> null;
        };
    }

    /** THÊM HÀM MỚI (Best Practice):
     *
     * Dịch một chuỗi 'key' (ví dụ: "brand.id") thành một đối tượng Path (đường dẫn)
     * trong Criteria API, tự động thực hiện JOIN các bảng liên quan
     *
     * @param root Root<T> của Entity
     * @param key  Key (ví dụ: "name" hoặc "brand.id" hoặc "category.parent.id")
     * @return Path<?> trỏ đến thuộc tính cuối cùng (Ví dụ: Cột 'id' của bảng 'Brand')
     */
    private Path<?> getPath(Root<T> root, String key) {
        // Case: tt của obj gốc (VD: key: "name" | "price" |...)
        if (!key.contains(".")) return root.get(key);

        // Case: có JOIN (ví dụ: "brand.id" | "category.parent.id" )
        String[] keys = key.split("\\."); // -> ["brand", "id"] | ["category", "parent", "id"]

        // Dùng 'From' để giữ tham chiếu đến đối tượng có thể 'join'
        // Bắt đầu từ 'root' (vì Root cũng là 'From')
        From<?, ?> join = root;

        // Duyệt qua tất cả các key TRỪ key cuối cùng
        // Vòng lặp này chạy cho "category" và "parent" (nếu key là "category.parent.name")
        for (int i = 0; i < keys.length - 1; i++) {
            // join = root.join("brand") | join.join("category") & join.join("parent")
            // SQL ⇔ FROM product p
            //              JOIN brand b ON p.brand_id = b.id
            //              ⇔ JOIN category c ON p.category_id = c.id
            //                JOIN category parent_c ON c.parent_id = parent_c.id
            join = join.join(keys[i]);
        }

        // Sau vòng lặp, 'join' đang trỏ đến bảng "parent" (ví dụ 2) hoặc "brand" (ví dụ 1)
        // Bây giờ ta lấy thuộc tính cuối cùng ("id" hoặc "name") từ bảng đó
        // 'join.get("id")' trả về Path<?>
        return join.get(keys[keys.length - 1]);
    }


    /**
     * Phương thức tiện ích để chuyển đổi giá trị String sang kiểu dữ liệu đích của trường Entity.
     *
     * @param fieldType Kiểu Java của trường Entity (e.g., Integer.class)
     * @param value     Giá trị đầu vào (luôn là String)
     * @return Đối tượng đã được ép kiểu (Integer, Double, Date, v.v.)
     */
    private Object castToRequiredType(Class<?> fieldType, Object value) {
        if (value == null) return null;

        if (fieldType.isEnum() && value instanceof String) { // Đích Emun → tìm Enum hằng số
            // Enum.valueOf(Status.class, "ACTIVE") return Status.ACTIVE
            return Enum.valueOf((Class<Enum>) fieldType, ((String) value).toUpperCase());
        }

        // --- Xử lý các kiểu cơ bản ---
        String stringValue = value.toString();

        if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            return Integer.parseInt(stringValue);
        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
            return Long.parseLong(stringValue);
        } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
            return Double.parseDouble(stringValue);
        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            // Xử lý "1"/"0", "true"/"false"
            return Boolean.parseBoolean(stringValue);
        } else if (fieldType.equals(LocalDate.class)) {
            // Cần đảm bảo định dạng ngày tháng đầu vào chuẩn (yyyy-MM-dd)
            try {
                return LocalDate.parse(stringValue);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format for LocalDate: " + stringValue);
            }
        } else if (fieldType.equals(LocalDateTime.class)) {
            try {
                return LocalDateTime.parse(stringValue);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format for LocalDateTime: " + stringValue);
            }
        } else if (fieldType.equals(UUID.class)) {
            return UUID.fromString(stringValue);
        }

        // Mặc định trả về giá trị chuỗi (áp dụng cho String.class hoặc các kiểu không được hỗ trợ)
        return stringValue;
    }

}
