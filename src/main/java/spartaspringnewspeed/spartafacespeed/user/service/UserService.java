package spartaspringnewspeed.spartafacespeed.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.config.encode.PasswordEncoder;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.common.exception.LoginException;
import spartaspringnewspeed.spartafacespeed.common.exception.UserNotFoundException;
import spartaspringnewspeed.spartafacespeed.common.exception.ValidateException;
import spartaspringnewspeed.spartafacespeed.user.model.dto.UserDto;
import spartaspringnewspeed.spartafacespeed.user.model.request.DeletionRequest;
import spartaspringnewspeed.spartafacespeed.user.model.request.LoginRequest;
import spartaspringnewspeed.spartafacespeed.user.model.request.SignUpRequest;
import spartaspringnewspeed.spartafacespeed.user.model.response.ProfileResponse;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto createUser(SignUpRequest request) {
        String encodePassword = passwordEncoder.encode(request.password());
        boolean isEmailExists = userRepository.existsByEmail(request.email());
        if(isEmailExists) {
            throw new ValidateException("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT);
        }
        User user = User.createUser(request, encodePassword, false); // isDeleted 처음에 false
        userRepository.save(user);
        return UserDto.convertDto(user);
    }

    public void softDeleteUser(DeletionRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("계정이 존재하지 않습니다."));
        if(passwordEncoder.matches(request.password(), user.getPassword())) {
            user.updateIsDeleted(true);
            userRepository.save(user);
        } else {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }
    }

    public Long getUserId(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("계정이 존재하지 않습니다."));

        if(user.isDeleted()){
            throw new LoginException("계정은 이미 삭제 되었습니다.");
        }

        if(passwordEncoder.matches(request.password(), user.getPassword())) {
            return user.getUserId();
        } else {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }
    }

    public ProfileResponse createProfile(Long userId){

        User user = userRepository.findByUserIdOrElseThrow(userId);

        return new ProfileResponse(userId, user.getUserName(), user.getEmail());
    }

    public List<ProfileResponse> findAllProfiles(){

        List<User> users = userRepository.findAll();

        return users.stream().map(ProfileResponse::toDto).toList();
    }


    public List<ProfileResponse> searchProfile(String ProfileName, String ProfileEmail){
        List<User> users = userRepository.findAll();
        List<ProfileResponse> responseList = new ArrayList<>();

        if(users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다.");
        }

        for(User user : users){
            if(user.getUserName().equals(ProfileName) || user.getEmail().equals(ProfileEmail)){
                responseList.add(new ProfileResponse(user.getUserId(), user.getUserName(), user.getEmail()));
            }
        }


        return responseList;
    }


    public ProfileResponse updateProfile(Long userId, String profileName, String profileEmail){

        User user = userRepository.findByUserIdOrElseThrow(userId);

        if(user.getEmail().equals(profileEmail)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }

        user.updateProfile(profileName,profileEmail);
        userRepository.save(user);

        return new ProfileResponse(user.getUserId(), user.getUserName(), user.getEmail());
    }


    public void updatePassword(Long userId, String oldPassword, String newPassword){
        User user = userRepository.findByUserIdOrElseThrow(userId);

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String encodePassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodePassword);
        userRepository.save(user);
    }

}
