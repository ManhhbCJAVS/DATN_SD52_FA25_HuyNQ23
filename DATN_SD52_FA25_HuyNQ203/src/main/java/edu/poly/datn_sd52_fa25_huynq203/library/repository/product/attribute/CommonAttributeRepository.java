package edu.poly.datn_sd52_fa25_huynq203.library.repository.product.attribute;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.CommonAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

// "KHUÔN MẪU" cho REPOSITORY chung.
@NoRepositoryBean
public interface CommonAttributeRepository<E extends CommonAttribute, ID extends Serializable>
        extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {
    // 📌 Thêm các phương thức cần cho logic nghiệp vụ (kiểm tra trùng lặp)
    Optional<E> findByName(String name);
    boolean existsByCode(String code);
    boolean existsByName(String name);

}