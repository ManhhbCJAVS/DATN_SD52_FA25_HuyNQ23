package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.CommonAttribute;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "gender")
public class Gender extends CommonAttribute {

}
