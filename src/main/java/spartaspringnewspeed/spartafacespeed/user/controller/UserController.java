package spartaspringnewspeed.spartafacespeed.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spartaspringnewspeed.spartafacespeed.user.model.dto.UserDto;
import spartaspringnewspeed.spartafacespeed.user.model.request.ProfileRequest;
import spartaspringnewspeed.spartafacespeed.user.model.response.ProfileResponse;
import spartaspringnewspeed.spartafacespeed.user.model.response.UserResponse;
import spartaspringnewspeed.spartafacespeed.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDto dto) {

        UserResponse user = userService.createUser(dto.getUserName(), dto.getEmail(), dto.getPassword());

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> allUsers = userService.getAllUsers();

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
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

}