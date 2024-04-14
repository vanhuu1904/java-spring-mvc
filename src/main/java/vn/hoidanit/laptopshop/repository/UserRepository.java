package vn.hoidanit.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.hoidanit.laptopshop.domain.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User hoidanit);
    void deleteById(long id);
    List<User> findByEmail(String email);
    List<User> findAll();
    User findById(long id);
    // User deleteById(long id);  
}
