package spartaspringnewspeed.spartafacespeed.friend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.common.entity.Friend;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendInfoResponse;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendResponse;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(Long requesterId, Long receiverId) throws Exception {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new Exception("Requester not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new Exception("Receiver not found"));

        // 자기 자신에게 친구 요청 불가
        if (requester.equals(receiver)) {
            throw new Exception("Cannot send friend request to yourself");
        }

        // 두 사용자 사이의 기존 친구 관계 확인 (양방향 검사)
        Optional<Friend> existingFriendship = friendRepository.findByRequesterAndReceiver(requester, receiver);
        Optional<Friend> reverseFriendship = friendRepository.findByRequesterAndReceiver(receiver, requester);

        if (existingFriendship.isPresent()) {
            FriendshipStatus status = existingFriendship.get().getStatus();
            if (status == FriendshipStatus.PENDING || status == FriendshipStatus.ACCEPTED) {
                throw new Exception("Friend request already sent or already friends");
            } else if (status == FriendshipStatus.DELETED) {
                // 상태를 PENDING으로 업데이트하고 저장
                Friend friendship = existingFriendship.get();
                friendship.setStatus(FriendshipStatus.PENDING);
                friendship.setRequester(requester);
                friendship.setReceiver(receiver);
                friendRepository.save(friendship);
                return;
            }
        }

        if (reverseFriendship.isPresent()) {
            FriendshipStatus status = reverseFriendship.get().getStatus();
            if (status == FriendshipStatus.PENDING || status == FriendshipStatus.ACCEPTED) {
                throw new Exception("Friend request already sent or already friends");
            } else if (status == FriendshipStatus.DELETED) {
                // 상태를 PENDING으로 업데이트하고 요청자와 수락자를 변경
                Friend friendship = reverseFriendship.get();
                friendship.setStatus(FriendshipStatus.PENDING);
                friendship.setRequester(requester);
                friendship.setReceiver(receiver);
                friendRepository.save(friendship);
                return;
            }
        }

        // 기존 친구 관계가 없으므로 새로운 친구 요청 생성
        Friend friendRequest = new Friend();
        friendRequest.setRequester(requester);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendshipStatus.PENDING);

        friendRepository.save(friendRequest);
    }

    @Transactional
    public List<FriendResponse> getPendingFriendRequests(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Friend> pendingRequests = friendRepository.findByReceiverAndStatus(user, FriendshipStatus.PENDING);

        return pendingRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    @Transactional
    public void accepteriendRequest(Long requestId, Long myId) throws Exception {
        User user = userRepository.findByUserIdOrElseThrow(myId);

        Friend requestedFriend = friendRepository.findByIdAndReceiverAndStatus(
                        requestId, user, FriendshipStatus.PENDING)
                .orElseThrow(() -> new Exception("Friend Relationship not found"));

        requestedFriend.setStatus(FriendshipStatus.ACCEPTED);

        // Check if the reversed friend relationship already exists
        Optional<Friend> existingReverseFriendOpt = friendRepository.findByRequesterAndReceiver(
                user, requestedFriend.getRequester());

        Friend reverseFriend;
        if (existingReverseFriendOpt.isPresent()) {
            // Update the existing relationship's status to ACCEPTED
            reverseFriend = existingReverseFriendOpt.get();
            reverseFriend.setStatus(FriendshipStatus.ACCEPTED);
        } else {
            // Create a new reversed friend relationship
            reverseFriend = new Friend();
            reverseFriend.setRequester(user);
            reverseFriend.setReceiver(requestedFriend.getRequester());
            reverseFriend.setStatus(FriendshipStatus.ACCEPTED);
        }

        // Save both relationships
        friendRepository.save(requestedFriend);
        friendRepository.save(reverseFriend);
    }

    @Transactional
    public void declineFriendRequest(Long requestId, Long myId) throws Exception {
        User user = userRepository.findByUserIdOrElseThrow(myId);

        Friend requestedFriend = friendRepository.findByIdAndReceiverAndStatus(
                        requestId, user, FriendshipStatus.PENDING)
                .orElseThrow(() -> new Exception("Friend request not found"));

        // 친구 요청 상태를 DECLINED로 변경하거나 삭제합니다.
        requestedFriend.setStatus(FriendshipStatus.DECLINED);
        friendRepository.save(requestedFriend);

        // 필요에 따라 추가 로직을 구현할 수 있습니다.
    }


    public List<FriendInfoResponse> getFriendsList(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        // 요청자로서의 ACCEPTED 상태 친구만 조회
        List<Friend> friends = friendRepository.findByRequesterAndStatus(user, FriendshipStatus.ACCEPTED);

        return friends.stream()
                .map(this::convertToFriendInfoResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteFriend(Long userId, Long friendId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        User friendUser = userRepository.findById(friendId)
                .orElseThrow(() -> new Exception("Friend user not found"));

        // Mark the friend relationship as DELETED
        Friend friendRelationship = friendRepository.findByRequesterAndReceiverAndStatus(
                        user, friendUser, FriendshipStatus.ACCEPTED)
                .orElseThrow(() -> new Exception("Friend relationship not found"));

        friendRelationship.setStatus(FriendshipStatus.DELETED);
        friendRepository.save(friendRelationship);

        // Mark the reverse friend relationship as DELETED
        Friend reverseFriendRelationship = friendRepository.findByRequesterAndReceiverAndStatus(
                        friendUser, user, FriendshipStatus.ACCEPTED)
                .orElseThrow(() -> new Exception("Friend relationship not found"));

        reverseFriendRelationship.setStatus(FriendshipStatus.DELETED);
        friendRepository.save(reverseFriendRelationship);
    }

    private FriendInfoResponse convertToFriendInfoResponse(Friend friend) {
        User otherUser = friend.getReceiver();

        return new FriendInfoResponse(
                otherUser.getUserId(),
                otherUser.getUserName(),
                otherUser.getEmail()
        );
    }

    private FriendResponse convertToDTO(Friend friend) {
        FriendResponse dto = new FriendResponse();
        dto.setId(friend.getId());
        dto.setRequesterId(friend.getRequester().getUserId());
        dto.setRequesterUsername(friend.getRequester().getUserName());
        dto.setReceiverId(friend.getReceiver().getUserId());
        dto.setReceiverUsername(friend.getReceiver().getUserName());
        dto.setStatus(friend.getStatus().name());
        dto.setCreatedAt(friend.getCreatedAt());
        dto.setUpdatedAt(friend.getUpdatedAt());
        return dto;
    }
}