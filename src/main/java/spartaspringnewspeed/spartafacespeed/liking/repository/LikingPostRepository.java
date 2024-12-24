package spartaspringnewspeed.spartafacespeed.liking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spartaspringnewspeed.spartafacespeed.common.entity.LikingPost;

public interface LikingPostRepository extends JpaRepository<LikingPost, Long> {

    /**
     * 좋아요 상태 표시
     * @param userId 유저식별자
     * @param postId 게시글 식별자
     * @return true : 좋아요를 이미 눌렀다. false : 안눌렀다.
     */
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM LikingPost l WHERE l.user.userId = :userId AND l.post.id = :postId")
    boolean existsByUserIdAndPostId( Long userId, Long postId);


    /**
     * 게시글의 좋아요 총 개수 출력
     * @param postId 게시글 식별자
     * @return 게시글의 좋아요 총 갯수
     */
    @Query("SELECT COUNT(l) FROM LikingPost l WHERE l.post.id = :postId")
    long countLikesByPostId(@Param("postId") Long postId);



}
