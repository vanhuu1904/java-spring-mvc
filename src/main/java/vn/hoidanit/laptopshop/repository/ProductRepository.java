package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hoidanit.laptopshop.domain.Product;
import java.util.List;
import java.util.Optional;

//crud: create, read, update, delete
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product save(Product product);
    List<Product> findAll();
    Optional<Product> findById(long id);
    void deleteById(long id);
}
