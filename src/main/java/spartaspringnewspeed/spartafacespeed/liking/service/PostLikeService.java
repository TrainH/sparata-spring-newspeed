package spartaspringnewspeed.spartafacespeed.liking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.common.entity.PostLike;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.liking.model.dto.PostLikeDto;
import spartaspringnewspeed.spartafacespeed.liking.repository.PostLikeRepository;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public String pushPostLike(Long postId, Long userId) {

        Post post = postRepository.findPostByIdOrThrow(postId);
        User user = userRepository.findByUserIdOrElseThrow(userId);
        PostLike postLike = new PostLike(user, post);

        if(postLikeRepository.existsByUser_UserIdAndPost_Id(userId,postId)){
            postLike = postLikeRepository.findByUser_UserIdAndPost_Id(userId,postId);
            if(postLike==null){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,"좋아요 기능의 예기치 않은 예외 발생");
            }
            postLikeRepository.delete(postLikeRepository.findByIdOrElseThrow(postLike.getId()));
            return "좋아요 취소 완료";
        }

        postLikeRepository.saveAndFlush(postLike);

        return "좋아요";
    }
}
