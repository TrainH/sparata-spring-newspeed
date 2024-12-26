package spartaspringnewspeed.spartafacespeed.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentDto;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentPagingDto;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    default Comment findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Does not exist id = " + id));
    }


    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    Page<Comment> findByPostIdOrderByLikeCountDescCreatedAtDesc(Long postId, Pageable pageable);

    long countByPost_Id(Long id);

}

