package com.back.bounded_context.post.out;

import com.back.bounded_context.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}