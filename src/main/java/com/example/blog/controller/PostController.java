package com.example.blog.controller;

import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.dto.PostDto;
import com.example.blog.entity.Tag;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    // 포스트 목록을 가져오고 정렬 기준에 따라 정렬
    @GetMapping("/posts")
    public String showPostList(@RequestParam(required = false) String sortBy, Model model) {
        List<Post> posts;
        if (sortBy != null) {
            posts = postService.findAllSorted(sortBy);
        } else {
            posts = postService.findAll();
        }

        // 포스트 ID와 태그 이름을 매핑하는 맵 생성
        Map<Long, String> postTagsMap = new HashMap<>();
        for (Post post : posts) {
            String tags = post.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.joining(", "));
            postTagsMap.put(post.getId(), tags);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("postTagsMap", postTagsMap);
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
        Post post = postService.findById(id);
        model.addAttribute("post", post);
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
        postService.update(id, postDto);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/posts";
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam String keyword, Model model) {
        List<Post> posts = postService.searchPosts(keyword);
        model.addAttribute("posts", posts);
        return "post_list";
    }
}