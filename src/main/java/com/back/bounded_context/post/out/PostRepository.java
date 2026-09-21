package com.back.bounded_context.post.out;

import com.back.bounded_context.post.domain.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @EntityGraph(attributePaths = "author")
    List<Post> findByOrderByIdDesc();

    @Override
    @EntityGraph(attributePaths = "author")
    Optional<Post> findById(Integer id);
}