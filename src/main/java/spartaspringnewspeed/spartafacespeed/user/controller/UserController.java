package spartaspringnewspeed.spartafacespeed.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spartaspringnewspeed.spartafacespeed.user.model.dto.UserDto;
import spartaspringnewspeed.spartafacespeed.user.model.request.DeletionRequest;
import spartaspringnewspeed.spartafacespeed.user.model.request.LoginRequest;
import spartaspringnewspeed.spartafacespeed.user.model.request.SignUpRequest;
import spartaspringnewspeed.spartafacespeed.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody SignUpRequest request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.OK);
    }

    @PatchMapping("/deletion")
    public ResponseEntity<UserDto> softDeleteUser(@Valid @RequestBody DeletionRequest request, HttpSession session) {
        userService.softDeleteUser(request);
        session.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> getUserId(@Valid @RequestBody LoginRequest request, HttpSession session) {
        Long userId = userService.getUserId(request);

        session.setAttribute("userId", userId);

        return new ResponseEntity<>("로그인 성공했습니다.", HttpStatus.OK);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity<>("로그아웃 성공했습니다.",HttpStatus.OK);
    }
}
