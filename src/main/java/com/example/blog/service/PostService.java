package com.example.blog.service;

import com.example.blog.dto.PostDto;
import com.example.blog.entity.Post;
import com.example.blog.entity.Tag;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    public List<Post> findAll() {
        return postRepository.findAll(); // 게시물 검색
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(); // id로 게시물 검색
    }

    public void save(PostDto postDto) {
        Post post = new Post();  // Post 객체 생성
        post.setTitle(postDto.getTitle());  // 제목 설정
        post.setContent(postDto.getContent()); // 내용 설정
        post.setCreatedAt(LocalDateTime.now());  // 현재 시간을 생성 시간으로 설정

        // 태그 설정
        List<Tag> tags = postDto.getTags().stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByName(tagName);
                    if (tag == null) {
                        tag = new Tag();
                        tag.setName(tagName);
                        tagRepository.save(tag);
                    }
                    return tag;
                }).collect(Collectors.toList());
        post.setTags(tags);

        postRepository.save(post);  // PostRepository의 save 메소드 호출하여 게시물을 데이터베이스에 저장
    }

    public void update(Long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        // 태그 설정
        List<Tag> tags = postDto.getTags().stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByName(tagName);
                    if (tag == null) {
                        tag = new Tag();
                        tag.setName(tagName);
                        tagRepository.save(tag);
                    }
                    return tag;
                }).collect(Collectors.toList());
        post.setTags(tags);

        postRepository.save(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleContaining(keyword);
    }

    public List<Post> findAllSorted(String sortBy) {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
    }
}