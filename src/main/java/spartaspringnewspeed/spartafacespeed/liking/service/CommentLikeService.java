package spartaspringnewspeed.spartafacespeed.liking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.comment.repository.CommentRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;
import spartaspringnewspeed.spartafacespeed.common.entity.CommentLike;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.common.exception.CannotLikeOwnContentException;
import spartaspringnewspeed.spartafacespeed.common.exception.IdValidationNotFoundException;
import spartaspringnewspeed.spartafacespeed.common.exception.NotOwnerActionException;
import spartaspringnewspeed.spartafacespeed.liking.repository.CommentLikeRepository;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public String pushCommentLike(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findPostByIdOrThrow(postId);
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        //해당 포스트의 코멘트가 아닐때
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IdValidationNotFoundException(postId);
        }

        User currentUser = userRepository.findByUserIdOrElseThrow(userId);

        //본인의 게시글에 좋아요를 누를 수 없음
        if (comment.getUser().getUserId().equals(userId)) {
            throw new CannotLikeOwnContentException();
        }

        if (commentLikeRepository.existsByUserAndComment(currentUser, comment)) {
            CommentLike commentLike = commentLikeRepository.findByUser_UserIdAndComment_Id(currentUser.getUserId(), comment.getId());
            commentLikeRepository.delete(commentLikeRepository.findByIdOrElseThrow(commentLike.getId()));
            comment.setLikeCount(comment.getLikeCount() - 1);
            commentRepository.save(comment);
            return "좋아요 취소";
        }

        CommentLike commentLike = new CommentLike(currentUser, comment);
        commentLikeRepository.save(commentLike);

        comment.setLikeCount(comment.getLikeCount() + 1);
        commentRepository.save(comment);

        return "좋아요";
    }
}

