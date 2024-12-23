package spartaspringnewspeed.spartafacespeed.friend.model.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendResponse {

    private Long id;
    private Long requesterId;
    private String requesterUsername;
    private Long receiverId;
    private String receiverUsername;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
