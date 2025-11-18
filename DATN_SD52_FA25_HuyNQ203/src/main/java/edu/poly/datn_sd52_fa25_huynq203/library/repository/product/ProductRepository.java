package edu.poly.datn_sd52_fa25_huynq203.library.repository.product;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByNameAndBrandId(String name, Long brandId);

    // Viết thêm hàm này dùng cho trang Chi tiết sản phẩm
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.variants WHERE p.id = :id")
    Optional<Product> findByIdWithVariants(@Param("id") Long id);
}
