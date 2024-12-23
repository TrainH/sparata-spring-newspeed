package spartaspringnewspeed.spartafacespeed.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;

    private String userName; // UserName;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static UserDto convertDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
