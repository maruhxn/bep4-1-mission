package com.back.bounded_context.member.listener;

import com.back.bounded_context.member.entity.Member;
import com.back.bounded_context.member.service.MemberService;
import com.back.global.event.PostCommentCreatedEvent;
import com.back.global.event.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MemberEventListener {
    private final MemberService memberService;

    private static final int POST_CREATE_ACTIVITY_SCORE = 3;
    private static final int COMMENT_CREATE_ACTIVITY_SCORE = 1;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(PostCreatedEvent event) {
        Member member = memberService.findById(event.post().authorId()).get();

        member.increaseActivityScore(POST_CREATE_ACTIVITY_SCORE);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(PostCommentCreatedEvent event) {
        Member member = memberService.findById(event.postComment().authorId()).get();

        member.increaseActivityScore(COMMENT_CREATE_ACTIVITY_SCORE);
    }

}
