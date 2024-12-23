package spartaspringnewspeed.spartafacespeed.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import spartaspringnewspeed.spartafacespeed.post.model.dto.PostDto;

@Controller
public class PostController {
    @GetMapping("/posts/new")
    public String newPostForm() {
        return "posts/new";
    }
    @PostMapping("/posts/create")
    public String createPost(PostDto postDto) {

        return "posts/create";
    }
}
