package spartaspringnewspeed.spartafacespeed.user.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeletionRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {
}
