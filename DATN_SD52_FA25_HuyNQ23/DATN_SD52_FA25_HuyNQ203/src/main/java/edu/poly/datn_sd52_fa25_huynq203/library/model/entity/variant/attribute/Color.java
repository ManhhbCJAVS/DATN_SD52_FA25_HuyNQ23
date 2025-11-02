package edu.poly.datn_sd52_fa25_huynq203.library.model.entity.variant.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.CommonAttribute;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "color")
public class Color extends CommonAttribute {
    @Column(name = "hex_code", nullable = false, length = 100)
    String hexCode;
}
