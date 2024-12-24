package spartaspringnewspeed.spartafacespeed.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.user.model.response.ProfileResponse;
import spartaspringnewspeed.spartafacespeed.user.model.response.UserResponse;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public UserResponse createUser (String userName, String email ,String password) {
        User user = new User(userName, email, password);
        User newUser = userRepository.save(user);

        return new UserResponse(newUser.getUserId(), newUser.getUserName(), newUser.getEmail());
    }

    public List<UserResponse> getAllUsers(){
//        List<User> users = new ArrayList<>();
//
//        for(User user : userRepository.findAll()){
//            users.add(user);
//        }
//
//        return  List<UserResponse> (users);
//        return new UserResponse(users.get(0).getUserId(), users.get(0).getUserName(), users.get(0).getEmail());
//        List<User> users = userRepository.findAll();
        return userRepository.findAll().stream().map(UserResponse::toDto).toList();
    }

    public User createProfile(Long userId){

        User user = userRepository.findByUserIdOrElseThrow(userId);

        user.Profile(user.getUserName(), user.getEmail());
        return userRepository.save(user);
    }

    public List<ProfileResponse> findAllProfiles(){
        return userRepository.findAll().stream().map(ProfileResponse::toDto).toList();
    }

    public List<ProfileResponse> searchProfile(String ProfileName, String ProfileEmail){
        List<User> users = userRepository.findAll();
        List<ProfileResponse> responseList = new ArrayList<>();

        if(users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        for(User user : users){
            if(user.getProfileName().equals(ProfileName) || user.getProfileEmail().equals(ProfileEmail)){
                responseList.add(new ProfileResponse(user.getUserId(), user.getUserName(), user.getEmail()));
            }

        }
        return responseList;
    }

    public ProfileResponse updateProfile(Long userId, String profileName, String profileEmail){
        User user = userRepository.findByUserIdOrElseThrow(userId);
        if(user.getProfileEmail().equals(profileEmail)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        user.updateProfile(profileName,profileEmail);
        userRepository.save(user);

        return new ProfileResponse(user.getUserId(), user.getUserName(), user.getEmail());
    }

}


