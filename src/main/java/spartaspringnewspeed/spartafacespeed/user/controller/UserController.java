package spartaspringnewspeed.spartafacespeed.user.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spartaspringnewspeed.spartafacespeed.user.model.dto.UserDto;
import spartaspringnewspeed.spartafacespeed.user.model.request.*;
import spartaspringnewspeed.spartafacespeed.user.model.response.ProfileResponse;
import spartaspringnewspeed.spartafacespeed.user.service.UserService;

import java.util.List;

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

    @PostMapping("/{userId}/profile")
    public ResponseEntity<ProfileResponse> createProfile(@PathVariable Long userId){

        ProfileResponse profile = userService.createProfile(userId);

        return new ResponseEntity<>(profile, HttpStatus.OK);

    }

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileResponse>> findAllProfiles() {

        List<ProfileResponse> allProfiles = userService.findAllProfiles();

        return new ResponseEntity<>(allProfiles, HttpStatus.OK);
    }

    @GetMapping("/profiles/search")
    public ResponseEntity<List<ProfileResponse>> searchProfile(@RequestParam String profileName, @RequestParam String profileEmail) {

        List<ProfileResponse> ProfileResponse = userService.searchProfile(profileName, profileEmail);

        return new ResponseEntity<>(ProfileResponse, HttpStatus.OK);
    }

    @PatchMapping("/{userId}/updateProfile")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable Long userId,@RequestBody ProfileRequest dto) {

        ProfileResponse profileResponse = userService.updateProfile(userId, dto.getProfileName(), dto.getProfileEmail());

        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }

    @PatchMapping("/{userId}/updatePassword")
    public ResponseEntity<ProfileResponse> updatePassword(@PathVariable Long userId,@RequestBody PasswordRequest dto) {
        userService.updatePassword(userId, dto.getOldPassword(), dto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
