package spartaspringnewspeed.spartafacespeed.friend.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.friend.model.request.FriendDeleteRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.request.FriendRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.request.FriendUpdateRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendInfoResponse;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendResponse;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.friend.service.FriendService;
import spartaspringnewspeed.spartafacespeed.user.model.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private FriendRepository friendRepository;

    // 친구 요청 처리
    @PostMapping("/requests")
    public ResponseEntity<String> sendFriendRequest(@Valid @RequestBody FriendRequest dto, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors occurred: " + bindingResult.getAllErrors());
        }

        try {
            Long requesterId = (Long) session.getAttribute("userId");
            friendService.sendFriendRequest(requesterId, dto.getReceiverId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fail: " + e.getMessage());
        }
    }

    // 친구 요청 페이지 이동
    @GetMapping("/requests")
    public ResponseEntity<List<FriendResponse>> getAllFriendRequests(HttpSession session) {
        try {
            Long receiverId = (Long) session.getAttribute("userId");
            List<FriendResponse> friendRequests = friendService.getPendingFriendRequests(receiverId);
            return ResponseEntity.ok(friendRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    // 받은 친구 요청 목록
    @GetMapping("/requests/pending")
    public ResponseEntity<?> viewFriendRequests(HttpSession session) {
        try {
            Long receiverId = (Long) session.getAttribute("userId");
            List<FriendResponse> pendingRequests = friendService.getPendingFriendRequests(receiverId);
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    // 친구 요청 상태 업데이트 (수락 또는 거절)
    @PatchMapping("/requests/{requestId}")
    public ResponseEntity<String> updateFriendRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody FriendUpdateRequest updateDto,
            BindingResult bindingResult,
            HttpSession session) {
        if (bindingResult.hasErrors()) {
            // 요청 데이터의 검증 오류 처리
            return ResponseEntity.badRequest().body("Invalid request data");
        }
        try {
            Long myId = (Long) session.getAttribute("userId");
            String status = updateDto.getStatus();

            if ("ACCEPTED".equalsIgnoreCase(status)) {
                friendService.accepteriendRequest(requestId, myId);
                return ResponseEntity.ok("Friend request accepted");
            } else if ("DECLINED".equalsIgnoreCase(status)) {
                friendService.declineFriendRequest(requestId, myId);
                return ResponseEntity.ok("Friend request declined");
            } else {
                return ResponseEntity.badRequest().body("Invalid status value");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }


    // 친구 목록 페이지
    @GetMapping("")
    public ResponseEntity<?> getFriends(HttpSession session) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            List<FriendInfoResponse> friends = friendService.getFriendsList(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{friendUserId}")
    public ResponseEntity<String> deleteFriend(@PathVariable Long friendUserId, HttpSession session) {

        // Get the current user's ID from the session
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            // User is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
        }

        try {
            // Call the service method to mark the friend relationship as DELETED
            friendService.deleteFriend(userId, friendUserId);
            return ResponseEntity.ok("Friend relationship has been deleted");
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error deleting friend: " + e.getMessage());
        }
    }

}
