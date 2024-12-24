package spartaspringnewspeed.spartafacespeed.user.model.response;

import lombok.Getter;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

@Getter
public class UserResponse {
    
    private Long userId;
    private String userName;
    private String email;
    
    public UserResponse() {}

    public UserResponse(Long userId, String userName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        
    }

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
    }

    public static UserResponse toDto(User user) {
        return new UserResponse(user.getUserId(), user.getUserName(), user.getEmail());
    }
}
