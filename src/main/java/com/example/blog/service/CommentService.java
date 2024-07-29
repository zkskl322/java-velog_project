package com.example.blog.service;

import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.dto.CommentDto;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void save(CommentDto commentDto) {
        Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new IllegalArgumentException("해당 ID의 게시물이 존재하지 않습니다."));
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public CommentDto getComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 존재하지 않습니다."));
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setContent(commentDto.getContent());
        commentDto.setPostId(comment.getPost().getId());
        return commentDto;
    }

    public void update(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 존재하지 않습니다."));
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }
}