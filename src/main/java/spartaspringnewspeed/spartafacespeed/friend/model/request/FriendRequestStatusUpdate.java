package spartaspringnewspeed.spartafacespeed.friend.model.request;

import lombok.Data;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;

@Data
public class FriendRequestStatusUpdate {
    Long originalRequesterId;
    FriendshipStatus status;

}
