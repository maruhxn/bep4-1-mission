package com.back.bounded_context.member.in;

import com.back.bounded_context.member.domain.Member;
import com.back.bounded_context.member.app.MemberFacade;
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
    private final MemberFacade memberFacade;

    private static final int POST_CREATE_ACTIVITY_SCORE = 3;
    private static final int COMMENT_CREATE_ACTIVITY_SCORE = 1;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(PostCreatedEvent event) {
        Member member = memberFacade.findById(event.post().authorId()).get();

        member.increaseActivityScore(POST_CREATE_ACTIVITY_SCORE);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(PostCommentCreatedEvent event) {
        Member member = memberFacade.findById(event.postComment().authorId()).get();

        member.increaseActivityScore(COMMENT_CREATE_ACTIVITY_SCORE);
    }

}
