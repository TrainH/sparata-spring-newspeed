package spartaspringnewspeed.spartafacespeed.common.exception;

import lombok.Getter;


public class NotFriendsErrorDto {
    public static final String READ_FAIL = "해당 유저와 친구가 아닙니다.";
    public String message;

    public NotFriendsErrorDto(String message) {
        this.message = message;
    }

    public String getReadFail() {
        return READ_FAIL;
    }
}
