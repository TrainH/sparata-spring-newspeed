package spartaspringnewspeed.spartafacespeed.post.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long commentCount;
    private Long likeCount;


    public PostResponse(PostDto postDto, Long commentCount, Long likeCount) {
        this.id = postDto.getId();
        this.content = postDto.getContent();
        this.userId = postDto.getUserId();
        this.createdAt = postDto.getCreatedAt();
        this.updatedAt = postDto.getUpdatedAt();
        this.commentCount = commentCount;
        this.likeCount = likeCount;
    }
}
