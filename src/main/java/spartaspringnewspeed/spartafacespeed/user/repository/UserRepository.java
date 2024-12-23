package spartaspringnewspeed.spartafacespeed.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long userId);

    default User findByUserIdOrElseThrow(Long userId) {
        return findByUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist userId = " + userId));
    }

    User findByEmail(String email);
    boolean existsByEmail(String email);

}
