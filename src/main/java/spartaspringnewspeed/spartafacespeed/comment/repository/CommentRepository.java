package spartaspringnewspeed.spartafacespeed.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentPagingDto;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    default Comment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id = " + id));
    }

    @Query("SELECT new spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentPagingDto(" +
            "c.id, c.user.userId, c.post.id, c.content, c.createdAt, c.updatedAt, COUNT(c.id)) " +
            "FROM Comment c WHERE c.post.id = :postId " +
            "GROUP BY c.id, c.user.userId, c.post.id, c.content, c.createdAt, c.updatedAt " +
            "ORDER BY c.createdAt DESC")
    Page<CommentPagingDto> findAllByPostIdWithCount(@Param("postId") Long postId, Pageable pageable);

    long countByPost_Id(Long id);

}

