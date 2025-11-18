package edu.poly.datn_sd52_fa25_huynq203.library.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public abstract class CommonAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    //TODO: generate unique code
    @Column(name = "code")
    String code = "BR-" + java.util.UUID.randomUUID().toString();

    @Column(name = "name")
    String name;

    @Column(name = "created_at")
    @CreationTimestamp //YYYY-MM-DDTHH:MM:SS
    LocalDateTime createdAt;

    @Column(name = "is_deleted")
    Boolean isDeleted = Boolean.FALSE;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [");
        if (id != null) {
            sb.append("id=").append(id).append(", ");
        }
        if (code != null) {
            sb.append("code=").append(code).append(", ");
        }
        if (name != null) {
            sb.append("name=").append(name).append(", ");
        }
        if (createdAt != null) {
            sb.append("createdAt=").append(createdAt).append(", ");
        }
        sb.append("isDeleted=").append(isDeleted);
        sb.append("]");
        return sb.toString();
    }
}