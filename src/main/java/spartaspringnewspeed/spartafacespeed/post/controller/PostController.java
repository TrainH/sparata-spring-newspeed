package spartaspringnewspeed.spartafacespeed.post.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostDto;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostPageDto;
import spartaspringnewspeed.spartafacespeed.post.model.request.CreatePostRequest;
import spartaspringnewspeed.spartafacespeed.post.model.request.UpdatePostRequest;
import spartaspringnewspeed.spartafacespeed.post.model.response.PostResponse;
import spartaspringnewspeed.spartafacespeed.post.service.PostService;
import jakarta.servlet.http.HttpSession;


import java.util.List;



@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @Autowired
    private HttpSession session;


    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostRequest createPostRequest, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        PostDto createdPost = postService.createPost(createPostRequest, userId);
        return ResponseEntity.ok(createdPost);
    }

//    시간순
    @GetMapping("/timeorder")
    public ResponseEntity<PostPageDto> getAllPostsByUpdatedAtDesc(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // page와 size를 전달받아서 PostService에서 처리한 결과를 반환
        PostPageDto postPageDto = postService.getPostsOrderByCreatedAtDesc(page-1, size);
        return ResponseEntity.ok(postPageDto);
    }

//    좋아요 순서 조회
    @GetMapping("/likeorder")
    public ResponseEntity<PostPageDto> getAllPostsByLikeCountDesc(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // page와 size를 전달받아서 PostService에서 처리한 결과를 반환
        PostPageDto postPageDto = postService.getPostsOrderByLikeCountDesc(page-1, size);
        return ResponseEntity.ok(postPageDto);
    }

    //게시물 단건조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        PostResponse postResponse = postService.getPostById(postId);
        return ResponseEntity.ok(postResponse);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        PostDto updatedPost = postService.updatePost(postId, updatePostRequest, userId);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        Long userId = (Long) session.getAttribute("userId");
        postService.deletePost(postId, userId);
        return ResponseEntity.noContent().build();
    }

}
