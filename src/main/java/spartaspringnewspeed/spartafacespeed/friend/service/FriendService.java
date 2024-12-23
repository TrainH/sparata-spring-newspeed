package spartaspringnewspeed.spartafacespeed.friend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.common.entity.Friend;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.friend.model.request.FriendRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendResponse;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(Long requesterId, FriendRequest friendRequestDTO) throws Exception {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new Exception("Requester not found"));

        User receiver = userRepository.findById(friendRequestDTO.getReceiverId())
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
    public void acceptFriendRequest(Long receiverId, Long friendRequestId) throws Exception {
        Friend friendRequest = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new Exception("Friend request not found"));

        if (!friendRequest.getReceiver().getUserId().equals(receiverId)) {
            throw new Exception("Unauthorized");
        }

        friendRequest.accept();
        friendRepository.save(friendRequest);
    }

    @Transactional
    public void declineFriendRequest(Long receiverId, Long friendRequestId) throws Exception {
        Friend friendRequest = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new Exception("Friend request not found"));

        if (!friendRequest.getReceiver().getUserId().equals(receiverId)) {
            throw new Exception("Unauthorized");
        }

        friendRequest.decline();
        friendRepository.save(friendRequest);
    }

    public List<FriendResponse> getPendingFriendRequests(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Friend> pendingRequests = friendRepository.findByReceiverAndStatus(user, FriendshipStatus.PENDING);

        return pendingRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<FriendResponse> getFriendsList(Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        List<Friend> acceptedFriends = friendRepository.findByRequesterAndStatus(user, FriendshipStatus.ACCEPTED);

        return acceptedFriends.stream().map(this::convertToDTO).collect(Collectors.toList());
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