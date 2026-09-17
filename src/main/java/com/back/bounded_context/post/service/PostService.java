package com.back.bounded_context.post.service;

import com.back.bounded_context.member.entity.Member;
import com.back.bounded_context.post.entity.Post;
import com.back.bounded_context.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public long count() {
        return postRepository.count();
    }

    public Post write(Member author, String title, String content) {
        Post post = new Post(author, title, content);

        // 게시글 작성 시, 활동 점수 +3
        author.increaseActivityScore(3);

        return postRepository.save(post);
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }
}