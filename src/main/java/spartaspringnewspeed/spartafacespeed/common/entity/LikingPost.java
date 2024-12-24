package spartaspringnewspeed.spartafacespeed.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(uniqueConstraints ={
        @UniqueConstraint(name = "user_like_post",
        columnNames = {
                "user_id",
                "post_id"
        })
})
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LikingPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;
}
