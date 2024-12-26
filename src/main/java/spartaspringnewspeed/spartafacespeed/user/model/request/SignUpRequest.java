package spartaspringnewspeed.spartafacespeed.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest (
        @NotBlank
        @Size(min = 4, max = 20, message = "유저명은 4글자 이상 20글자 이하로 입력해야 합니다.")
        String userName,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 최소 8글자 이상이며, 대문자, 소문자, 숫자, 특수문자를 각각 최소 1글자 이상 포함해야 합니다."
        )
        String password
){
}
