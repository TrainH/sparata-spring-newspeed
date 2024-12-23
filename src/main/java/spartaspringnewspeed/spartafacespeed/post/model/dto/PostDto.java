package spartaspringnewspeed.spartafacespeed.post.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String postContent;
    private String userName;
    private String postTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
