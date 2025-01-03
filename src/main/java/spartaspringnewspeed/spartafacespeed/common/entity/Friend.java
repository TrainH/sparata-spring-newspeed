package spartaspringnewspeed.spartafacespeed.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Friend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 친구 요청을 보낸 사용자
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    // 친구 요청을 받은 사용자
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    // 친구 요청 상태 (예: PENDING, ACCEPTED, DECLINED)
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;


    // 상태 변경 메서드
    public void accept() {
        this.status = FriendshipStatus.ACCEPTED;
    }

    public void decline() {
        this.status = FriendshipStatus.DECLINED;
    }
}