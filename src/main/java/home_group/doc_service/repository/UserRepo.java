package home_group.doc_service.repository;

import home_group.doc_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);

    boolean existsByEmail(String email);

}
