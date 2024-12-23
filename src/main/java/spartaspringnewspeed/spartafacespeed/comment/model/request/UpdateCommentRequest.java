package spartaspringnewspeed.spartafacespeed.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateCommentRequest {

    @NotBlank
    @Size(max = 100)
    String content;

}
