package spartaspringnewspeed.spartafacespeed.post.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public PostDto(Post post) {
        this.id = post.getId();
        this.content = post.getContent();
        this.userId = post.getUser().getUserId();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }


}
