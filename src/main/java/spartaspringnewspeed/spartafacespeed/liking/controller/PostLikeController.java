package spartaspringnewspeed.spartafacespeed.liking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spartaspringnewspeed.spartafacespeed.liking.model.dto.PostLikeDto;
import spartaspringnewspeed.spartafacespeed.liking.service.PostLikeService;

@RestController
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<String> pushPostLike(@PathVariable Long postId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(postLikeService.pushPostLike(postId,userId), HttpStatus.CREATED);
    }

}
