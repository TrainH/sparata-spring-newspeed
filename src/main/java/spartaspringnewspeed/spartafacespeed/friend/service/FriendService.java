package spartaspringnewspeed.spartafacespeed.friend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.common.entity.Friend;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.friend.model.request.FriendRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.request.TestUserRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendInfoResponse;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendResponse;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    public void setTestUser(TestUserRequest userRequest) {
        User user = new User(userRequest.getUserName(), userRequest.getEmail(), userRequest.getPassword());
//        User user = new User("망곰이", "MG22@gmail.com", "123456");

        userRepository.save(user);
    }

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

        // 이미 친구 요청이 있거나 친구인 경우 확인
        if (friendRepository.existsByRequesterAndReceiverAndStatus(requester, receiver, FriendshipStatus.PENDING) ||
                friendRepository.existsByRequesterAndReceiverAndStatus(requester, receiver, FriendshipStatus.ACCEPTED)) {
            throw new Exception("Friend request already sent or already friends");
        }

        // 친구 요청 생성
        Friend friendRequest = new Friend();
        friendRequest.setRequester(requester);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendshipStatus.PENDING);

        friendRepository.save(friendRequest);
    }

    @Transactional
    public void confirmFriendRequest(Long originalReceiverId, Long originalRequesterId, FriendshipStatus status) throws Exception {
        User originalRequester = userRepository.findByUserId(originalRequesterId)
                .orElseThrow(() -> new Exception("Requester not found"));
        User originalReceiver = userRepository.findByUserId(originalReceiverId)
                .orElseThrow(() -> new Exception("Requester not found"));
        Friend requestedFriend = friendRepository.findByRequesterAndReceiverAndStatus(originalRequester, originalReceiver, FriendshipStatus.PENDING)
                .orElseThrow(() -> new Exception("Friend Relationship not found"));

        requestedFriend.setStatus(status);

        friendRepository.save(requestedFriend);
    }


    public List<FriendResponse> getPendingFriendRequests(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Friend> pendingRequests = friendRepository.findByReceiverAndStatus(user, FriendshipStatus.PENDING);

        return pendingRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FriendInfoResponse> getFriendsList(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Friend> requesterFriends = friendRepository.findByRequesterAndStatus(user, FriendshipStatus.ACCEPTED);
        List<Friend> receiverFriends = friendRepository.findByReceiverAndStatus(user, FriendshipStatus.ACCEPTED);

        List<Friend> allFriends = new ArrayList<>();
        allFriends.addAll(requesterFriends);
        allFriends.addAll(receiverFriends);

        return allFriends.stream()
                .map(friend -> convertToFriendInfoResponse(friend, user))
                .collect(Collectors.toList());
    }

    private FriendInfoResponse convertToFriendInfoResponse(Friend friend, User currentUser) {
        User otherUser;

        if (friend.getRequester().equals(currentUser)) {
            otherUser = friend.getReceiver();
        } else {
            otherUser = friend.getRequester();
        }

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
