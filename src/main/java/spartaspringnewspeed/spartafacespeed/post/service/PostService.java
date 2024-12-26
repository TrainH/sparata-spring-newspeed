package spartaspringnewspeed.spartafacespeed.post.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.comment.repository.CommentRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.Comment;
import spartaspringnewspeed.spartafacespeed.common.entity.FriendshipStatus;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.friend.repository.FriendRepository;
import spartaspringnewspeed.spartafacespeed.liking.repository.PostLikeRepository;
import spartaspringnewspeed.spartafacespeed.post.model.request.CreatePostRequest;
import spartaspringnewspeed.spartafacespeed.post.model.request.UpdatePostRequest;
import spartaspringnewspeed.spartafacespeed.post.model.response.PostResponse;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostDto;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostPageDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final FriendRepository friendRepository;


    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, PostLikeRepository postLikeRepository, FriendRepository friendRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
        this.friendRepository = friendRepository;
    }

    //게시물 등록
    @Transactional
    public PostDto createPost(CreatePostRequest postDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        Post post = new Post(postDto.getContent(), user);
        Post savedPost = postRepository.save(post);

        return new PostDto(savedPost);
    }

    // 친구 관련, 좋아요 많은 순,  관련 정렬
    //    기간별 검색기능
    //페이제네이션
    //    public PostPageDto getPostpeed(int page, int size) {


    public PostPageDto getPostsOrderByCreatedAtDesc(Long userId, int page, int size) {
        List<Long> friendIds = friendRepository.findFriendUserIdsByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
        friendIds.add(userId); // Include the user's own posts

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<Post> postPage = postRepository.findByUser_UserIdIn(friendIds, pageable);
        Page<PostDto> postDtoPage = postPage.map(PostDto::new);
        return PostPageDto.fromPage(postDtoPage);
    }

    public PostPageDto getPostsOrderByLikeCountDesc(Long userId, int page, int size){
        List<Long> friendIds = friendRepository.findFriendUserIdsByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
        friendIds.add(userId); // Include the user's own posts

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("likeCount")));
        Page<Post> postPage = postRepository.findByUser_UserIdIn(friendIds, pageable);

        Page<PostDto> postDtoPage = postPage.map(PostDto::new);

        return PostPageDto.fromPage(postDtoPage);

    }


    //게시물 단건조회
    public PostResponse getPostById(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        List<Long> friendIds = friendRepository.findFriendUserIdsByUserIdAndStatus(userId, FriendshipStatus.ACCEPTED);
        friendIds.add(userId); // Include the user's own posts


        if (!friendIds.contains(post.getUser().getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can view posts of friends only");
        }


        long commentCount = commentRepository.countByPost_Id(postId);
        long likeCount = postLikeRepository.countByPost_Id(postId);

        PostDto postDto = new PostDto(post);

        return new PostResponse(postDto, commentCount, likeCount);
    }

    //게시물 수정
    @Transactional
    public PostDto updatePost(Long postId, UpdatePostRequest updatePostRequest, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to edit this post");
        }
        post.setContent(updatePostRequest.getContent());

        Post updatedPost = postRepository.save(post);
        return new PostDto(updatedPost);
    }

    //게시물 삭제
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        if (!post.getUser().getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to delete this post");
        }

        postRepository.delete(post);

    }

}
