package spartaspringnewspeed.spartafacespeed.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import spartaspringnewspeed.spartafacespeed.common.entity.Post;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostDto;

@Controller
public class PostController {
    @PostMapping("/posts")
    public String createPost(PostDto postDto) {
        return "posts";
    }
}
