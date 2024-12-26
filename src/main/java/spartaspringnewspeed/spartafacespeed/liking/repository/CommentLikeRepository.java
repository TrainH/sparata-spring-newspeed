package spartaspringnewspeed.spartafacespeed.liking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;
import spartaspringnewspeed.spartafacespeed.common.entity.CommentLike;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    long countByComment_Id(Long id);

    CommentLike findByUser_UserIdAndComment_Id(Long userId, Long commentId);

    boolean existsByUserAndComment(User user, Comment comment);

    default CommentLike findByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
