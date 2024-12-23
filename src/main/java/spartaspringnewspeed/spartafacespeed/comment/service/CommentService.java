package spartaspringnewspeed.spartafacespeed.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.comment.model.dto.CommentDto;
import spartaspringnewspeed.spartafacespeed.comment.model.request.CreateCommetRequest;
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
//    private final PostRepository postRepository;
//    private final UserRepository userRepository;


    /**
     * 댓글 생성
     * @param request 댓글 내용
     * @param userId 세션 - 유저 아이디
     * @param postId URL - 게시글 아이디
     * @return CommentDto
     */
    public CommentDto createComment(CreateCommetRequest request,long userId, long postId) {
//        Post post = postRepository.findByOrElseThrow(postId);
//        User user = userRepository.findByOrElseThrow(post.getUser.getId());
//
//        Comment saveComment = new Comment(request.getContent(),user, post);
//
//        commentRepository.save(saveComment);
//
//        return new CommentDto(commentRepository.findByIdOrElseThrow(saveComment.getCommentId()));
        return new CommentDto();
    }

//    /**
//     * 댓글 조회(전체, 페이지)
//     * @param postId 게시글 아이디
//     * @param pageable 페이지 객체
//     * @return Page<CommentDto>
//     */
//    public Page<CommentDto> getCommentsByPostId(Long postId, Pageable pageable) {
//        Page<CommentDto> commentListPage = commentRepository.findByPostId(postId,pageable);
//        return commentListPage;
//    }
//
//    /**
//     * 댓글 수정(본인만 수정 가능)
//     * @param request 댓글 내용
//     * @param userId 유저 식별자
//     * @param postId 게시글 식별자
//     * @param commentId 댓글 식별자
//     * @return
//     */
//    @Transactional
//    public CommentDto updateComment(UpdateCommentRequest request, Long userId,Long postId, Long commentId){
//        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
//
//        if(!comment.getPost().getId().equals(postId)) {
//            //예외처리
//        }
//
//        Post post = postRepository.findByOrElseThrow(postId);
//        User user = userRepository.findByOrElseThrow(post.getUser.getId());
//
//        if(!user.getId().equals(userId)) {
//            //예외처리
//        }
//
//        commentRepository.saveAndFlush(comment);
//        return new CommentDto(comment,user,post);
//    }
//

//    /**
//     *
//     * @param userId 유저 식별자
//     * @param postId 게시글 식별자
//     * @param commentId 댓글 식별자
//     * @return Void, controller에서 반환 시 String 타입 출력
//     */
//    @Transactional
//    public String deleteComment(Long userId, Long postId, Long commentId){
//        Comment comment = commentRepository.findByIdOrElseThrow(commentId);
//
//        if(!comment.getPost().getId().equals(postId)) {
//            //예외처리
//        }
//
//        Post post = postRepository.findByOrElseThrow(postId);
//        User user = userRepository.findByOrElseThrow(post.getUser.getId());
//
//        if(!user.getId().equals(userId)) {
//            //예외처리
//        }
//
//        commentRepository.delete(comment);
//    return "삭제완료";
//    }


}
