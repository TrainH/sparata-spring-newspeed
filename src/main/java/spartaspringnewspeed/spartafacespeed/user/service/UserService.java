package spartaspringnewspeed.spartafacespeed.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import spartaspringnewspeed.spartafacespeed.common.config.encode.PasswordEncoder;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.common.exception.LoginException;
import spartaspringnewspeed.spartafacespeed.common.exception.ValidateException;
import spartaspringnewspeed.spartafacespeed.user.model.dto.UserDto;
import spartaspringnewspeed.spartafacespeed.user.model.request.DeletionRequest;
import spartaspringnewspeed.spartafacespeed.user.model.request.SignUpRequest;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

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
        User user = userRepository.findByEmail(request.email());
        if(passwordEncoder.matches(request.password(), user.getPassword())) {
            user.updateIsDeleted(true);
            userRepository.save(user);
        } else {
            throw new LoginException("비밀번호가 일치하지 않습니다.");
        }
    }

}
