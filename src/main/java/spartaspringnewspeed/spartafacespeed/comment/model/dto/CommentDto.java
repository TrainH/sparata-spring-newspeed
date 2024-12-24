package spartaspringnewspeed.spartafacespeed.comment.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long userId;
    private Long postId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentDto(Comment comment) {
        this.commentId = comment.getId();
        this.userId = comment.getUser().getUserId();
        this.postId = comment.getPost().getId();
        this.userName = comment.getUser().getUserName();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getUpdatedAt();
    }
}
