package com.back.bounded_context.post.app;

import com.back.bounded_context.post.domain.Post;
import com.back.bounded_context.post.domain.PostMember;
import com.back.global.dto.RsData;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostFacade {
    private final PostSyncMemberUseCase postSyncMemberUseCase;
    private final PostWriteUseCase postWriteUseCase;
    private final PostSupport postSupport;

    @Transactional
    public PostMember syncMember(MemberDto member) {
        return postSyncMemberUseCase.syncMember(member);
    }

    @Transactional
    public RsData<Post> write(PostMember author, String title, String content) {
        return postWriteUseCase.write(author, title, content);
    }

    @Transactional(readOnly = true)
    public long count() {
        return postSupport.count();
    }


    @Transactional(readOnly = true)
    public Optional<Post> findById(int id) {
        return postSupport.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<PostMember> findMemberByUsername(String username) {
        return postSupport.findMemberByUsername(username);
    }
}