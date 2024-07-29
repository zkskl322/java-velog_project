package com.example.blog.controller;

import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.dto.PostDto;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.findAll();  // 모든 게시물 검색
        model.addAttribute("posts", posts);  // 검색된 게시물을 모델에 추가. 모델은 뷰에 데이터를 전달
        return "post_list";
    }

    @PostMapping("/create")
    public String createPost(PostDto postDto) {
        postService.save(postDto);
        return "redirect:/posts";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        return "post_form";
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Post post = postService.findById(id); // id로 게시물 검색
        model.addAttribute("post", post); // 검색된 게시물을 모델에 추가
        List<Comment> comments = commentService.findByPostId(id);
        model.addAttribute("comments", comments);
        return "post_detail";
    }

    @GetMapping("/posts/{id}/update")
    public String update(@PathVariable Long id, Model model) {
        Post post = postService.findById(id);
        model.addAttribute("post", post);
        return "post_update";
    }

    @PostMapping("/posts/{id}/update")
    public String updatePost(@PathVariable Long id, PostDto postDto) {
        postService.update(id, postDto); // 게시물 수정
        return "redirect:/posts/" + id; // 수정된 게시물의 세부 정보 페이지로 리다이렉트
    }

    @GetMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }
}