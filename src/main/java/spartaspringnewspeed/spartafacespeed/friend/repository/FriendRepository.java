package spartaspringnewspeed.spartafacespeed.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spartaspringnewspeed.spartafacespeed.common.entity.Friend;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 특정 사용자 간의 친구 요청 조회
    Optional<Friend> findByRequesterAndReceiverAndStatus(User requester, User receiver, FriendshipStatus status);
    Optional<Friend> findByRequesterAndReceiver(User requester, User receiver);


    Optional<Friend> findByIdAndReceiverAndStatus(Long id, User receiver, FriendshipStatus status);


    // 특정 사용자의 수락된 친구 목록 조회
    List<Friend> findByRequesterAndStatus(User requester, FriendshipStatus status);

    List<Friend> findByReceiverAndStatus(User receiver, FriendshipStatus status);

    // 친구인지 확인
    boolean existsByRequesterAndReceiverAndStatus(User requester, User receiver, FriendshipStatus status);

    // 친구 중복 양방향 검사
    @Query("SELECT COUNT(f) > 0 FROM Friend f WHERE ((f.requester = :user1 AND f.receiver = :user2) OR (f.requester = :user2 AND f.receiver = :user1)) AND f.status IN :statuses")
    boolean existsFriendshipBetweenUsers(@Param("user1") User user1, @Param("user2") User user2, @Param("statuses") List<FriendshipStatus> statuses);

    @Query("SELECT CASE WHEN f.requester.userId = :userId THEN f.receiver.userId ELSE f.requester.userId END " +
            "FROM Friend f " +
            "WHERE (f.requester.userId = :userId OR f.receiver.userId = :userId) AND f.status = :status")
    List<Long> findFriendUserIdsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") FriendshipStatus status);
}