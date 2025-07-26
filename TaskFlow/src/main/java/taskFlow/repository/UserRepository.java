package taskFlow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taskFlow.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}

