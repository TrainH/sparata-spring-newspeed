package spartaspringnewspeed.spartafacespeed.comment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentPagingDto {
    private Long commentId;
    private Long userId;
    private Long postId;
    private String content;
}
