package spartaspringnewspeed.spartafacespeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentDto;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentPagingDto;
import spartaspringnewspeed.spartafacespeed.comment.model.request.CreateCommentRequest;
import spartaspringnewspeed.spartafacespeed.comment.model.request.UpdateCommentRequest;
import spartaspringnewspeed.spartafacespeed.comment.repository.CommentRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    /**
     * 댓글 생성
     * @param request 댓글 내용
     * @param userId 세션 - 유저 아이디
     * @param postId URL - 게시글 아이디
     * @return CommentDto
     */
    public CommentDto createComment(CreateCommentRequest request, long userId, long postId) {
        Post post = postRepository.findPostByIdOrThrow(postId);//댓글을 작성할 포스트 찾기
        User user = userRepository.findByUserIdOrElseThrow(userId);//댓글 작성 유저 찾기

        Comment saveComment = new Comment(request.getContent(),user, post); //해당 포스트에 입력할 댓글 만들기

        commentRepository.save(saveComment);

        return new CommentDto(commentRepository.findByIdOrElseThrow(saveComment.getId()));
    }

    /**
     * 댓글 조회(전체, 페이지)
     * @param postId 게시글 아이디
     * @param pageable 페이지 객체
     * @return Page<CommentDto>
     */
    public Page<CommentPagingDto> getCommentsByPostId(Long postId, Pageable pageable) {
        return commentRepository.findAllByPostIdWithCount(postId,pageable);
    }

    /**
     * 댓글 수정(본인만 수정 가능)
     * @param request 댓글 내용
     * @param userId 유저 식별자
     * @param postId 게시글 식별자
     * @param commentId 댓글 식별자
     * @return CommentDto
     */
    @Transactional
    public CommentDto updateComment(UpdateCommentRequest request, Long userId, Long postId, Long commentId){
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(!comment.getPost().getId().equals(postId)) {
            //예외처리
        }

        Post post = postRepository.findPostByIdOrThrow(postId);
        User user = userRepository.findByUserIdOrElseThrow(post.getUser().getUserId());;

        if(!user.getUserId().equals(userId)) {
            //예외처리
        }

        commentRepository.saveAndFlush(comment);
        return new CommentDto(comment);
    }


    /**
     *
     * @param userId 유저 식별자
     * @param postId 게시글 식별자
     * @param commentId 댓글 식별자
     * @return Void, controller에서 반환 시 String 타입 출력
     */
    @Transactional
    public String deleteComment(Long userId, Long postId, Long commentId){
        Comment comment = commentRepository.findByIdOrElseThrow(commentId);

        if(!comment.getPost().getId().equals(postId)) {
            //예외처리
        }

        Post post = postRepository.findPostByIdOrThrow(postId);
        User user = userRepository.findByUserIdOrElseThrow(post.getUser().getUserId());

        if(!user.getUserId().equals(userId)) {
            //예외처리
        }

        commentRepository.delete(comment);
    return "삭제완료";
    }


}
