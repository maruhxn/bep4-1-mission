package com.back.bounded_context.post.app;

import com.back.bounded_context.member.domain.Member;
import com.back.bounded_context.post.domain.Post;
import com.back.bounded_context.post.domain.PostMember;
import com.back.bounded_context.post.out.PostMemberRepository;
import com.back.bounded_context.post.out.PostRepository;
import com.back.global.dto.RsData;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostFacade {
    private final PostRepository postRepository;
    private final PostMemberRepository postMemberRepository;
    private final PostWriteUseCase postWriteUseCase;

    @Transactional(readOnly = true)
    public long count() {
        return postRepository.count();
    }

    @Transactional
    public RsData<Post> write(Member author, String title, String content) {
        return postWriteUseCase.write(author, title, content);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    public PostMember syncMember(MemberDto member) {
        var postMember = new PostMember(
                member.id(),
                member.username(),
                "",
                member.nickname(),
                member.createDate(),
                member.modifyDate()
        );

        return postMemberRepository.save(postMember);
    }
}