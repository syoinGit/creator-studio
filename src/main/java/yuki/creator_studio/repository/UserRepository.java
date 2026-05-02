package yuki.creator_studio.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yuki.creator_studio.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailAndDeletedFalse(String email);

  boolean existsByEmailAndDeletedFalse(String email);
}
