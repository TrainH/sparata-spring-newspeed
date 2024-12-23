package spartaspringnewspeed.spartafacespeed.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long userId);

}
