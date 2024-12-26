package spartaspringnewspeed.spartafacespeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentDto;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentPagingDto;
import spartaspringnewspeed.spartafacespeed.comment.model.request.CreateCommentRequest;
import spartaspringnewspeed.spartafacespeed.comment.model.request.UpdateCommentRequest;
import spartaspringnewspeed.spartafacespeed.comment.repository.CommentRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.*;
import spartaspringnewspeed.spartafacespeed.common.exception.IdValidationNotFoundException;
import spartaspringnewspeed.spartafacespeed.common.exception.NotFriendsException;
import spartaspringnewspeed.spartafacespeed.common.exception.NotOwnerActionException;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    /**
     * 댓글 생성
     *
     * @param request 댓글 내용
     * @param userId  세션 - 유저 아이디
     * @param postId  URL - 게시글 아이디
     * @return CommentDto
     */
    public CommentDto createComment(CreateCommentRequest request, long userId, long postId) {
        Post post = postRepository.findPostByIdOrThrow(postId);//댓글을 작성할 포스트 찾기
        User user = userRepository.findByUserIdOrElseThrow(userId);//댓글 작성 유저 찾기

        //포스트를 작성한 유저의 친구
        verifyFriends(post, userId);


        Comment saveComment = new Comment(request.getContent(), user, post); //해당 포스트에 입력할 댓글 만들기

        commentRepository.save(saveComment);

        return new CommentDto(commentRepository.findByIdOrElseThrow(saveComment.getId()));
    }

    /**
     * 댓글 조회(전체, 페이지)
     *
     * @param postId   게시글 아이디
     * @param pageable 페이지 객체
     * @return Page<CommentDto>
     */
    public Page<CommentPagingDto> getCommentsByPostId(Long postId,Long userId, Pageable pageable) {
        Post post = postRepository.findPostByIdOrThrow(postId);
        verifyFriends(post, userId);
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId,pageable).map(CommentPagingDto::new);
    }

    public Page<CommentPagingDto> getCommetsByPostIdOrderByLikeCount(Long postId, Long userId, Pageable pageable) {
        Post post = postRepository.findPostByIdOrThrow(postId);
        verifyFriends(post, userId);
        return commentRepository.findByPostIdOrderByLikeCountDescCreatedAtDesc(postId,pageable).map(CommentPagingDto::new);
    }

    /**
     * 댓글 수정(본인만 수정 가능)
     *
     * @param request   댓글 내용
     * @param userId    유저 식별자
     * @param postId    게시글 식별자
     * @param commentId 댓글 식별자
     * @return CommentDto
     */
    @Transactional
    public CommentDto updateComment(UpdateCommentRequest request, Long userId, Long postId, Long commentId) {
        Comment comment = getComment(userId, postId, commentId);
        comment.setContent(request.getContent());

        commentRepository.saveAndFlush(comment);
        return new CommentDto(comment);
    }


    /**
     * @param userId    유저 식별자
     * @param postId    게시글 식별자
     * @param commentId 댓글 식별자
     * @return Void, controller에서 반환 시 String 타입 출력
     */
    @Transactional
    public String deleteComment(Long userId, Long postId, Long commentId) {
        Comment comment = getComment(userId, postId, commentId);

        commentRepository.delete(comment);
        return "삭제완료";
    }

    private Comment getComment(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if (!comment.getPost().getId().equals(postId)) {
            throw new IdValidationNotFoundException(postId);
        }

        User user = userRepository.findByUserIdOrElseThrow(comment.getUser().getUserId());

        if (!user.getUserId().equals(userId)) {
            throw new NotOwnerActionException();
        }
        return comment;
    }


    /**
     * 친구 검증 메소드
     * @param post 해당 포스트
     * @param userId 현재 접속한 유저
     */
    private void verifyFriends(Post post, Long userId){//댓글을 작성할 포스트 찾기
        if(post.getUser().getUserId().equals(userId)){
            return;
        }
        //포스트를 작성한 유저의 친구 목록
        List<Long> friend = friendRepository.findFriendUserIdsByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);

        int cnt =0;
        for(Long friendsId : friend) {
            if(Objects.equals(friendsId, userId)){
                cnt ++;
                break;
            }
        }
        if(cnt==0) {
            throw new NotFriendsException("CommentService");
        }
    }


}
