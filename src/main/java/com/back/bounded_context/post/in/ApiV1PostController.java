package com.back.bounded_context.post.in;

import com.back.bounded_context.post.app.PostFacade;
import com.back.bounded_context.post.domain.Post;
import com.back.shared.post.dto.PostDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostFacade postFacade;

    @GetMapping
    public List<PostDto> getItems() {
        return postFacade
                .findByOrderByIdDesc()
                .stream()
                .map(Post::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public PostDto getItem(@PathVariable int id) {
        return postFacade
                .findById(id)
                .map(Post::toDto)
                .get();
    }
}