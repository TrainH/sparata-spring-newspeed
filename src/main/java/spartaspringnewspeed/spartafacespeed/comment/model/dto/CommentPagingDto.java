package spartaspringnewspeed.spartafacespeed.comment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Long commentCount;
}
