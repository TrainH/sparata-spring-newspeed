package spartaspringnewspeed.spartafacespeed.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class ProfileRequest {
    @Size(min = 4, max = 20, message = "유저명은 4글자 이상 20글자 이하로 입력해야 합니다.")
    private String profileName;
    @Email
    private String profileEmail;

    public ProfileRequest(String profileName, String profileEmail) {
        this.profileName = profileName;
        this.profileEmail = profileEmail;
    }
}
