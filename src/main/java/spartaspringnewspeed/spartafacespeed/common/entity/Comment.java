package spartaspringnewspeed.spartafacespeed.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Post post;

//    public Comment(String content, User user, Post post) {
//        this.content = content;
//        this.user = user;
//        this.post = post;
//    }

}
