package spartaspringnewspeed.spartafacespeed.friend.model.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestUserRequest {

    @NotNull
    private final String userName;

    @NotNull
    private final String email;

    @NotNull
    private final String password;

}
