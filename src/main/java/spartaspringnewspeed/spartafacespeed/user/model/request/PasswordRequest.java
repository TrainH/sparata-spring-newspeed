package spartaspringnewspeed.spartafacespeed.user.model.request;

import lombok.Getter;

@Getter
public class PasswordRequest {

    private final String oldPassword;
    private final String newPassword;

    public PasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
