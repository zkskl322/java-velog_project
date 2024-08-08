
package com.example.blog.repository;

import com.example.blog.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    UserLikes findByUserIdAndCommentId(Long userId, Long commentId);
}