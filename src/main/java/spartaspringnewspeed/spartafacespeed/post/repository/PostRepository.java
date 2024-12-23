package spartaspringnewspeed.spartafacespeed.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.common.entity.User;


import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByPostId(Long postId);

    public Post findPostById(Long postId) {
    return postRepository.findByPostId(postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist postId = " + postId));
}

    Optional<User> findByUserId(Long userId);

}
