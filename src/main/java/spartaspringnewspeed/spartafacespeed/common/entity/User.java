package spartaspringnewspeed.spartafacespeed.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    @Column
    private String profileName;
    @Column
    private String profileEmail;



    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void Profile(String profileName, String profileEmail) {
        this.profileName = profileName;
        this.profileEmail = profileEmail;
    }
    public void updateProfile(String profileName, String profileEmail) {
        this.profileName = profileName;
        this.userName = profileName;
        this.profileEmail = profileEmail;
        this.email = profileEmail;
    }


}
