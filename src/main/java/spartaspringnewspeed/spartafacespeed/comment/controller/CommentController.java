package spartaspringnewspeed.spartafacespeed.comment.controller;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentDto;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentPagingDto;
import spartaspringnewspeed.spartafacespeed.comment.model.request.CreateCommentRequest;
import spartaspringnewspeed.spartafacespeed.comment.model.request.UpdateCommentRequest;
import spartaspringnewspeed.spartafacespeed.comment.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId,
                                                    @RequestBody CreateCommentRequest request,
                                                    HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        return new ResponseEntity<>(commentService.createComment(request,userId,postId), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<CommentPagingDto>> getComments(@PathVariable Long postId,
                                                              @RequestParam(defaultValue = "5") int pageSize,
                                                              @RequestParam(defaultValue = "1") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize);
        return new ResponseEntity<>(commentService.getCommentsByPostId(postId,pageable),HttpStatus.OK);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody UpdateCommentRequest request,
                                                    HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        return new ResponseEntity<>(commentService.updateComment(request,userId,postId,commentId),HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              HttpSession session) {
        Long userId = (Long)session.getAttribute("userId");
        return new ResponseEntity<>(commentService.deleteComment(userId,postId,commentId),HttpStatus.OK);
    }

}
