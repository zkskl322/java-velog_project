package com.example.blog.controller;

import com.example.blog.dto.CommentDto;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String updateComment(@PathVariable Long id, @RequestParam String content, @RequestParam Long postId) {
        commentService.updateComment(id, content);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/comments/{id}/like")
    public String likeComment(@PathVariable Long id, @RequestParam Long postId, Authentication authentication, Model model) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            commentService.likeComment(id, username);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/posts/" + postId + "?error=" + e.getMessage();
        }
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/comments/{id}/unlike")
    public String unlikeComment(@PathVariable Long id, @RequestParam Long postId, Authentication authentication, Model model) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            commentService.unlikeComment(id, username);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/posts/" + postId + "?error=" + e.getMessage();
        }
        return "redirect:/posts/" + postId;
    }
}