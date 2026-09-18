package com.back.bounded_context.post.app;

import com.back.bounded_context.post.domain.Post;
import com.back.bounded_context.post.domain.PostMember;
import com.back.bounded_context.post.out.PostMemberRepository;
import com.back.bounded_context.post.out.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostSupport {
    private final PostRepository postRepository;
    private final PostMemberRepository postMemberRepository;

    public long count() {
        return postRepository.count();
    }

    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    public Optional<PostMember> findMemberByUsername(String username) {
        return postMemberRepository.findByUsername(username);
    }
}
