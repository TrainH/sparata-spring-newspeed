package spartaspringnewspeed.spartafacespeed.friend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FriendRequest {

    @NotNull
    private Long receiverId; // 친구 요청을 받을 사용자 ID

    @NotNull
    private Long requesterId;
}