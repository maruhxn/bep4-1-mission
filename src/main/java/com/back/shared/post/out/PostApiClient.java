package com.back.shared.post.out;

import com.back.bounded_context.post.domain.Post;
import com.back.shared.post.dto.PostDto;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PostApiClient {
    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://localhost:8080/api/v1/post")
            .build();

    public List<Post> getPosts() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public PostDto getPost(int id) {
        return restClient.get()
                .uri("/posts/%d".formatted(id))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
