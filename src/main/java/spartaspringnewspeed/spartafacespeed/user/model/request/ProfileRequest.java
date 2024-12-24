package spartaspringnewspeed.spartafacespeed.user.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ProfileRequest {
    private String profileName;
    private String profileEmail;

    public ProfileRequest(String profileName, String profileEmail) {
        this.profileName = profileName;
        this.profileEmail = profileEmail;
    }
}
