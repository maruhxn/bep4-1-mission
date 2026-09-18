package com.back.bounded_context.post.app;

import com.back.bounded_context.post.domain.PostMember;
import com.back.bounded_context.post.out.PostMemberRepository;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSyncMemberUseCase {
    private final PostMemberRepository postMemberRepository;

    public PostMember syncMember(MemberDto member) {
        var postMember = new PostMember(
                member.id(),
                member.username(),
                "",
                member.nickname(),
                member.activityScore(),
                member.createDate(),
                member.modifyDate()
        );

        return postMemberRepository.save(postMember);
    }
}
