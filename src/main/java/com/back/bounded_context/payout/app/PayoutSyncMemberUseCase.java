package com.back.bounded_context.payout.app;

import com.back.bounded_context.payout.domain.PayoutMember;
import com.back.bounded_context.payout.out.PayoutMemberRepository;
import com.back.global.event_publisher.EventPublisher;
import com.back.shared.member.dto.MemberDto;
import com.back.shared.payout.event.PayoutMemberCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayoutSyncMemberUseCase {
    private final PayoutMemberRepository payoutMemberRepository;
    private final EventPublisher eventPublisher;

    public PayoutMember syncMember(MemberDto member) {
        boolean isNew = !payoutMemberRepository.existsById(member.id());

        PayoutMember payoutMember = payoutMemberRepository.save(
                new PayoutMember(
                        member.id(),
                        member.username(),
                        "",
                        member.nickname(),
                        member.activityScore(),
                        member.createDate(),
                        member.modifyDate()
                )
        );

        if (isNew) {
            eventPublisher.publish(
                    new PayoutMemberCreatedEvent(
                            payoutMember.toDto()
                    )
            );
        }

        return payoutMember;
    }
}