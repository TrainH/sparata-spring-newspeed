package spartaspringnewspeed.spartafacespeed.post.model.dto;

import java.time.LocalDateTime;

public class PostDto {
    private String postContent;
    private String userName;
    private String postTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostDto() {
        this.postTitle=postTitle;
        this.postContent=postContent;
        this.userName=userName;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }


}
