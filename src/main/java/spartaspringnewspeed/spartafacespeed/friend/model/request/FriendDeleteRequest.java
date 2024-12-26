package spartaspringnewspeed.spartafacespeed.friend.model.request;

import jakarta.validation.constraints.NotNull;

public class FriendDeleteRequest {

    @NotNull(message = "Friend ID is required")
    private Long friendUserId;

    // Getters and setters

    public Long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(Long friendId) {
        this.friendUserId = friendId;
    }
}