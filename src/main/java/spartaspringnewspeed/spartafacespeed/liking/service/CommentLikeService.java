package spartaspringnewspeed.spartafacespeed.liking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spartaspringnewspeed.spartafacespeed.comment.repository.CommentRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.common.exception.IdValidationNotFoundException;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final PostLikeService postLikeService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public String pushCommentLike(Long postId, Long userId, Long commentId) {
        Post post = postRepository.findPostByIdOrThrow(postId);
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        //해당 포스트의 코멘트가 아닐때
        if(!comment.getPost().getId().equals(post.getId())) {
            throw new IdValidationNotFoundException(postId);
        }

        if(comment.getId().equals(commentId)) {
            throw new
        }


        User user = userRepository.findByUserIdOrElseThrow(userId);
    }
}
