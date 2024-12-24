package spartaspringnewspeed.spartafacespeed.friend.model.response;

import lombok.Data;

@Data
public class FriendInfoResponse {
    private Long friendId;
    private String username;
    private String email;

    // 필요한 필드들을 추가할 수 있습니다.

    public FriendInfoResponse(Long friendId, String username, String email) {
        this.friendId = friendId;
        this.username = username;
        this.email = email;
    }
}