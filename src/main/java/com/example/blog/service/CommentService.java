package com.example.blog.service;

import com.example.blog.UserLikes;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Member;
import com.example.blog.entity.Post;
import com.example.blog.dto.CommentDto;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.MemberRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserLikesRepository userLikesRepository;
    private final MemberRepository memberRepository;

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
        commentDto.setContent(comment.getContent());
        commentDto.setPostId(comment.getPost().getId());
        commentDto.setLikes(comment.getLikes());
        return commentDto;
    }

    public void updateComment(Long id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + id));
        comment.setContent(content);
        commentRepository.save(comment);
    }

    public void likeComment(Long commentId, String username) {
        Member member = memberRepository.findByUsername(username);
        if (userLikesRepository.existsByUserIdAndCommentId(member.getId(), commentId)) {
            throw new IllegalStateException("You have already liked this comment");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));

        // UserLikes 테이블에 좋아요 기록 추가
        UserLikes userLikes = new UserLikes();
        userLikes.setUserId(member.getId());
        userLikes.setCommentId(commentId);
        userLikesRepository.save(userLikes);

        // 댓글의 좋아요 수 증가
        comment.setLikes(comment.getLikes() + 1);
        commentRepository.save(comment);
    }

    public void unlikeComment(Long commentId, String username) {
        Member member = memberRepository.findByUsername(username);
        if (!userLikesRepository.existsByUserIdAndCommentId(member.getId(), commentId)) {
            throw new IllegalStateException("You have not liked this comment yet");
        }

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("Invalid comment Id:" + commentId));

        // UserLikes 테이블에서 좋아요 기록 삭제
        UserLikes userLikes = userLikesRepository.findByUserIdAndCommentId(member.getId(), commentId);
        userLikesRepository.delete(userLikes);

        // 댓글의 좋아요 수 감소
        comment.setLikes(comment.getLikes() - 1);
        commentRepository.save(comment);
    }

    public void decreaseLikes(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당 ID의 댓글이 존재하지 않습니다."));
        comment.setLikes(comment.getLikes() - 1);
        commentRepository.save(comment);
    }
}