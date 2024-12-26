package spartaspringnewspeed.spartafacespeed.liking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.common.entity.PostLike;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.common.exception.CannotLikeOwnContentException;
import spartaspringnewspeed.spartafacespeed.common.exception.NotFriendsException;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.liking.model.dto.PostLikeDto;
import spartaspringnewspeed.spartafacespeed.liking.repository.PostLikeRepository;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public String pushPostLike(Long postId, Long userId) {

        Post post = postRepository.findPostByIdOrThrow(postId);

        if(post.getUser().getUserId().equals(userId)) {
            throw new CannotLikeOwnContentException();
        }

        verifyFriends(post, userId);


        User user = userRepository.findByUserIdOrElseThrow(userId);



        if (postLikeRepository.existsByUserAndPost(user, post)) {
            PostLike postLike = postLikeRepository.findByUser_UserIdAndPost_Id(userId,postId);
            if(postLike==null){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"좋아요 기능의 예기치 않은 예외 발생");
            }
            postLikeRepository.delete(postLikeRepository.findByIdOrElseThrow(postLike.getId()));

            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            return "좋아요 취소";
        }

        // PostLike 저장
        PostLike postLike = new PostLike(user, post);
        postLikeRepository.save(postLike);

        // likeCount 증가
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);

        return "좋아요";
    }

    private void verifyFriends(Post post, Long userId){//댓글을 작성할 포스트 찾기
        if(post.getUser().getUserId().equals(userId)){
            return;
        }
        //포스트를 작성한 유저의 친구 목록
        List<Long> friend = friendRepository.findFriendUserIdsByUserIdAndStatus(post.getUser().getUserId(), FriendshipStatus.ACCEPTED);

        int cnt =0;
        for(Long friendsId : friend) {
            if(Objects.equals(friendsId, userId)){
                cnt ++;
                break;
            }
        }
        if(cnt==0) {
            throw new NotFriendsException("PostLikeService");
        }
    }

}
