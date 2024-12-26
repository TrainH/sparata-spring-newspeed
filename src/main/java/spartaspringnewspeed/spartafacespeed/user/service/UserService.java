package spartaspringnewspeed.spartafacespeed.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
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

    public void softDeleteUser(Long userId, DeletionRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("계정이 존재하지 않습니다."));

        if (!user.getUserId().equals(userId)) {
            throw new LoginException("본인 계정만 탈퇴 가능합니다.");
        }

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

    public ProfileResponse getMyProfile(Long userId){

        User user = userRepository.findByUserIdOrElseThrow(userId);

        return new ProfileResponse(userId, user.getUserName(), user.getEmail());
    }

    public List<ProfileResponse> findAllProfiles(){

        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            throw new ValidateException("유저를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        return users.stream().map(ProfileResponse::toDto).toList();
    }


    public List<ProfileResponse> searchProfile(String ProfileName, String ProfileEmail){
        List<User> users = userRepository.findAll();
        List<ProfileResponse> responseList = new ArrayList<>();

        if(users.isEmpty()){
            throw new ValidateException("유저를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
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

        if(profileEmail != null && !profileEmail.isEmpty()) {
            if (user.getEmail().equals(profileEmail)) {
                throw new ValidateException("이미 있는 이메일 입니다.", HttpStatus.BAD_REQUEST);
            }

            user.updateProfileEmail(profileEmail);

        }

        if (profileName != null && !profileName.isEmpty()) {
                user.updateProfileName(profileName);
            }

        userRepository.save(user);

        return new ProfileResponse(user.getUserId(), user.getUserName(), user.getEmail());
    }


    public void updatePassword(Long userId, String oldPassword, String newPassword){
        User user = userRepository.findByUserIdOrElseThrow(userId);

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new ValidateException("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }else if(passwordEncoder.matches(newPassword, user.getPassword())){
            throw new ValidateException("현재 비밀번호로와 동일한 비밀번호로는 변경할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        String encodePassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodePassword);
        userRepository.save(user);
    }

}
