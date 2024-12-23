package spartaspringnewspeed.spartafacespeed.friend.controller;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spartaspringnewspeed.spartafacespeed.friend.model.request.FriendRequest;
import spartaspringnewspeed.spartafacespeed.friend.model.response.FriendResponse;
import spartaspringnewspeed.spartafacespeed.friend.service.FriendService;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    // 친구 요청 페이지 이동
    @GetMapping("/request")
    public String showFriendRequestForm(Model model) {
        model.addAttribute("friendRequestDTO", new FriendRequest());
        return "friends/requestFriend";
    }

    // 친구 요청 처리
    @PostMapping("/request")
    public String sendFriendRequest(@Valid @ModelAttribute FriendRequest friendRequestDTO,
                                    BindingResult bindingResult,
                                    @SessionAttribute("userId") Long userId,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            return "friends/requestFriend";
        }

        try {
            friendService.sendFriendRequest(userId, friendRequestDTO);
            model.addAttribute("successMessage", "Friend request sent successfully");
            return "redirect:/friends/requests";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "friends/requestFriend";
        }
    }

    // 받은 친구 요청 목록
    @GetMapping("/requests")
    public String viewFriendRequests(@SessionAttribute("userId") Long userId, Model model) {
        try {
            List<FriendResponse> pendingRequests = friendService.getPendingFriendRequests(userId);
            model.addAttribute("pendingRequests", pendingRequests);
            return "friends/friendRequests";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    // 친구 요청 수락
    @PostMapping("/requests/{id}/accept")
    public String acceptFriendRequest(@SessionAttribute("userId") Long userId,
                                      @PathVariable("id") Long friendRequestId,
                                      Model model) {
        try {
            friendService.acceptFriendRequest(userId, friendRequestId);
            return "redirect:/friends/requests";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    // 친구 요청 거절
    @PostMapping("/requests/{id}/decline")
    public String declineFriendRequest(@SessionAttribute("userId") Long userId,
                                       @PathVariable("id") Long friendRequestId,
                                       Model model) {
        try {
            friendService.declineFriendRequest(userId, friendRequestId);
            return "redirect:/friends/requests";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    // 친구 목록 페이지
    @GetMapping("/list")
    public String listFriends(@SessionAttribute("userId") Long userId, Model model) {
        try {
            List<FriendResponse> friends = friendService.getFriendsList(userId);
            model.addAttribute("friends", friends);
            return "friends/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}