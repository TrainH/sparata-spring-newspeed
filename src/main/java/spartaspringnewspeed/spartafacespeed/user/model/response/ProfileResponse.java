package spartaspringnewspeed.spartafacespeed.user.model.response;


import lombok.Getter;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

@Getter
public class ProfileResponse {

    private Long userId;

    private String profileName;

    private String profileEmail;

    public ProfileResponse(Long userId, String profileName, String profileEmail) {
        this.userId = userId;
        this.profileName = profileName;
        this.profileEmail = profileEmail;
    }

        public static ProfileResponse toDto(User user) {

        return new ProfileResponse(user.getUserId(), user.getUserName(), user.getEmail());
    }

}
