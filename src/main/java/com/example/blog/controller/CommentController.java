package com.example.blog.controller;

import com.example.blog.dto.CommentDto;
import com.example.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public String createComment(@ModelAttribute CommentDto commentDto) {
        commentService.save(commentDto);
        return "redirect:/posts/" + commentDto.getPostId();
    }

    @GetMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id, @RequestParam Long postId) {
        commentService.delete(id);
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/comments/{id}/update")
    public String CommentsForm(@PathVariable Long id, Model model) {
        CommentDto commentDto = commentService.getComment(id);
        model.addAttribute("comment", commentDto);
        return "post_detail";
    }

    @PostMapping("/comments/{id}/update")
    public String updateComment(@PathVariable Long id, @ModelAttribute CommentDto commentDto) {
        commentService.update(id, commentDto);
        return "redirect:/posts/" + commentDto.getPostId();
    }
}