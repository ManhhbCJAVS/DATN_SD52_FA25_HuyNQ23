package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.CommonAttribute;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.ToString;

@Entity
@Table(name = "brand")
@ToString
public class Brand extends CommonAttribute {
}
