package com.back.bounded_context.member.app;

import com.back.bounded_context.member.domain.Member;
import com.back.global.dto.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberJoinUseCase memberJoinUseCase;
    private final MemberGetRandomSecureTipUseCase memberGetRandomSecureTipUseCase;
    private final MemberSupport memberSupport;


    @Transactional
    public RsData<Member> join(String username, String password, String nickname) {
        return memberJoinUseCase.join(username, password, nickname);
    }

    @Transactional(readOnly = true)
    public long count() {
        return memberSupport.count();
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(int id) {
        return memberSupport.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberSupport.findByUsername(username);
    }

    public String getRandomSecureTip() {
        return memberGetRandomSecureTipUseCase.getRandomSecureTip();
    }
}