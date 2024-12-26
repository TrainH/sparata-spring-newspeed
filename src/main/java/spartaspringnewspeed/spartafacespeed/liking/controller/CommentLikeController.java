package spartaspringnewspeed.spartafacespeed.liking.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import spartaspringnewspeed.spartafacespeed.liking.service.CommentLikeService;

@RestController
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/posts/{postId}/comments/{commentId}/likes")
    public ResponseEntity<String> pushCommentLike(@PathVariable Long postId, @PathVariable Long commentId, HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        return new ResponseEntity<>(commentLikeService.pushCommentLike(postId,commentId,userId), HttpStatus.CREATED);

    }

}
