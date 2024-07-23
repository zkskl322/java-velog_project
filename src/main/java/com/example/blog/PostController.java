package com.example.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;



    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.findAll();  // 모든 게시물 검색
        model.addAttribute("posts", posts);  // 검색된 게시물을 모델에 추가. 모델은 뷰에 데이터를 전달
        return "post_list";
    }
}