package spartaspringnewspeed.spartafacespeed.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spartaspringnewspeed.spartafacespeed.user.model.request.SignUpRequest;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private boolean isDeleted;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public static User createUser(SignUpRequest request, String encodePassword, boolean isDeleted) {
        return new User(null, request.userName(), request.email(), encodePassword, isDeleted);
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateProfile(String profileName, String profileEmail) {
        this.userName = profileName;
        this.email = profileEmail;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
