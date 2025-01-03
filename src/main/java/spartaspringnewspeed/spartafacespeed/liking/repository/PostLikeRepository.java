package spartaspringnewspeed.spartafacespeed.liking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.entity.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    /**
     * 좋아요 상태 표시
     * @param userId 유저식별자
     * @param id 게시글 식별자
     * @return true : 좋아요를 이미 눌렀다. false : 안눌렀다.
     */
    boolean existsByUser_UserIdAndPost_Id(Long userId, Long id);

    /**
     * 게시글의 좋아요 총 개수 출력
     * @param id 게시글 식별자
     * @return 게시글의 좋아요 총 갯수
     */
    long countByPost_Id(Long id);

    PostLike findByUser_UserIdAndPost_Id(Long userId, Long id);

    default PostLike findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Like Id를 찾을 수 없습니다."));
    }
}
