package spartaspringnewspeed.spartafacespeed.friend.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendUpdateRequest {

    @NotNull(message = "Status is required")
    private String status;


}