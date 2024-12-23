package spartaspringnewspeed.spartafacespeed.comment.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommetRequest {

    @Size(max = 100)
    @NotBlank
    String content;
}
