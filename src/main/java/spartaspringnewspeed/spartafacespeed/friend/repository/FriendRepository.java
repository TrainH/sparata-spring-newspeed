package spartaspringnewspeed.spartafacespeed.friend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.Friend;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    // 특정 사용자 간의 친구 요청 조회
    Optional<Friend> findByRequesterAndReceiver(User requester, User receiver);
    Optional<Friend> findByRequesterAndReceiverAndStatus(User requester, User receiver, FriendshipStatus status);

    // 특정 사용자의 수락된 친구 목록 조회
    List<Friend> findByRequesterAndStatus(User requester, FriendshipStatus status);

    List<Friend> findByReceiverAndStatus(User receiver, FriendshipStatus status);

    // 친구인지 확인
    boolean existsByRequesterAndReceiverAndStatus(User requester, User receiver, FriendshipStatus status);
}