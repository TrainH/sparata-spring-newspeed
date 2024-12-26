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
public class CommentPagingDto {
    private Long commentId;
    private Long userId;
    private Long postId;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private int LikeCount;

    public CommentPagingDto(Comment comment) {
        this.commentId = comment.getId();
        this.userId = comment.getUser().getUserId();
        this.postId = comment.getPost().getId();
        this.content = comment.getContent();
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getUpdatedAt();
        this.LikeCount = comment.getLikeCount();

    }
}
