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
import spartaspringnewspeed.spartafacespeed.friend.model.request.FriendRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendInfoResponse;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendResponse;
import spartaspringnewspeed.spartafacespeed.friend.service.FriendService;
import spartaspringnewspeed.spartafacespeed.user.model.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    // 친구 요청 페이지 이동
    @GetMapping("/request")
    public ResponseEntity<FriendRequest> showFriendRequestForm() {
        FriendRequest friendRequest = new FriendRequest();
        return ResponseEntity.ok(friendRequest);
    }

    // 친구 요청 처리
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@Valid @RequestBody FriendRequest friendRequestDTO, BindingResult bindingResult, HttpSession session)
    {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors occurred: " + bindingResult.getAllErrors());
        }

        try {
            Long requesterId = (Long) session.getAttribute("userId");
            friendService.sendFriendRequest(requesterId, friendRequestDTO.getReceiverId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail: " + e.getMessage());
        }
    }

    // 받은 친구 요청 목록
    @GetMapping("/requests")
    public ResponseEntity<?> viewFriendRequests(HttpSession session) {
        try {
            Long receiverId = (Long) session.getAttribute("userId");
            List<FriendResponse> pendingRequests = friendService.getPendingFriendRequests(receiverId);
            return ResponseEntity.ok(pendingRequests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // 친구 요청 변경
    @PostMapping("/requests/")
    public ResponseEntity<String> acceptFriendRequest(@RequestParam("status") FriendshipStatus status,
                                                      @RequestParam("originalRequesterId") Long originalRequesterId,
                                                      HttpSession session) {
        log.info(status.toString());
        try {
            Long userId = (Long) session.getAttribute("userId");
            friendService.confirmFriendRequest(userId, originalRequesterId, status);
            return ResponseEntity.ok("Friend request changed");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}