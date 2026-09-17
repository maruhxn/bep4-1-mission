package com.back.bounded_context.post.app;

import com.back.bounded_context.member.domain.Member;
import com.back.bounded_context.post.domain.Post;
import com.back.bounded_context.post.out.PostRepository;
import com.back.global.dto.PostDto;
import com.back.global.dto.RsData;
import com.back.global.event.EventPublisher;
import com.back.global.event.PostCreatedEvent;
import com.back.shared.member.out.MemberApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostWriteUseCase {
    private final PostRepository postRepository;
    private final EventPublisher eventPublisher;
    private final MemberApiClient memberApiClient;

    public RsData<Post> write(Member author, String title, String content) {
        Post post = postRepository.save(new Post(author, title, content));

        // 게시글 작성 시, 활동 점수 +3
        eventPublisher.publish(new PostCreatedEvent(new PostDto(post)));

        String randomSecureTip = memberApiClient.getRandomSecureTip();

        return new RsData<>(
                "201-1",
                "%d번 글이 생성되었습니다. 보안팁: %s".formatted(post.getId(), randomSecureTip),
                post);
    }
}
