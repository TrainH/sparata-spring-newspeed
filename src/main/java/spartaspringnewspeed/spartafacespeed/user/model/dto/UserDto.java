package spartaspringnewspeed.spartafacespeed.user.model.dto;

import lombok.Getter;

@Getter
public class UserDto {

    private String userName;
    private String email;
    private String password;


    public UserDto(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;

    }
}
