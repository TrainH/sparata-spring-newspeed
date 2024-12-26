package spartaspringnewspeed.spartafacespeed.liking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.comment.repository.CommentRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.*;
import spartaspringnewspeed.spartafacespeed.common.exception.CannotLikeOwnContentException;
import spartaspringnewspeed.spartafacespeed.common.exception.IdValidationNotFoundException;
import spartaspringnewspeed.spartafacespeed.common.exception.NotFriendsException;
import spartaspringnewspeed.spartafacespeed.common.exception.NotOwnerActionException;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.liking.repository.CommentLikeRepository;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final FriendRepository friendRepository;

    @Transactional
    public String pushCommentLike(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findPostByIdOrThrow(postId);

        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        //본인의 게시글에 좋아요를 누를 수 없음
        if (comment.getUser().getUserId().equals(userId)) {
            throw new CannotLikeOwnContentException();
        }

        verifyFriends(post, userId);

        //해당 포스트의 코멘트가 아닐때
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new IdValidationNotFoundException(postId);
        }

        User currentUser = userRepository.findByUserIdOrElseThrow(userId);

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
            throw new NotFriendsException("CommentLikeService");
        }
    }
}

