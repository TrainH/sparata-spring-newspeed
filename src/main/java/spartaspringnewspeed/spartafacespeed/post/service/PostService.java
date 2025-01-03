package spartaspringnewspeed.spartafacespeed.post.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spartaspringnewspeed.spartafacespeed.common.entity.User;
import spartaspringnewspeed.spartafacespeed.post.model.request.CreatePostRequest;
import spartaspringnewspeed.spartafacespeed.post.model.request.UpdatePostRequest;
import spartaspringnewspeed.spartafacespeed.post.model.response.PostResponse;
import spartaspringnewspeed.spartafacespeed.post.repository.PostRepository;
import spartaspringnewspeed.spartafacespeed.user.repository.UserRepository;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostDto;
import org.springframework.web.server.ResponseStatusException;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostPageDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
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

    //페이제네이션
    public PostPageDto getPostpeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("updatedAt")));
        Page<Post> postPage = postRepository.findAll(pageable);

        Page<PostDto> postDtoPage = postPage.map(PostDto::new);

        return PostPageDto.fromPage(postDtoPage);
    }


    //게시물 단건조회
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        return new PostResponse(post);
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
