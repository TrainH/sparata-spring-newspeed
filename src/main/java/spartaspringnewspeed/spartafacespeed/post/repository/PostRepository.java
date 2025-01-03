package spartaspringnewspeed.spartafacespeed.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;


import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

  Optional<Post> findById(Long id);

    default Post findPostByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist postId = " + id));
    }

}
