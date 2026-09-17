package com.back.bounded_context.post.repository;

import com.back.bounded_context.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}