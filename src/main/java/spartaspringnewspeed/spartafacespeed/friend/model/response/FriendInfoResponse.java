package spartaspringnewspeed.spartafacespeed.friend.model.response;

import lombok.Data;

@Data
public class FriendInfoResponse {
    private Long friendId;
    private String username;
    private String email;

    public FriendInfoResponse(Long friendId, String username, String email) {
        this.friendId = friendId;
        this.username = username;
        this.email = email;
    }
}