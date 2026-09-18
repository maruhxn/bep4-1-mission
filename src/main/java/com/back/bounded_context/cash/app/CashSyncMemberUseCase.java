package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashMember;
import com.back.bounded_context.cash.out.CashMemberRepository;
import com.back.global.event_publisher.EventPublisher;
import com.back.shared.cash.dto.CashMemberDto;
import com.back.shared.cash.event.CashMemberCreatedEvent;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashSyncMemberUseCase {
    private final CashMemberRepository cashMemberRepository;
    private final EventPublisher eventPublisher;

    /**
     * syncMember()가 새 회원일 때 CashMemberCreatedEvent 발행 -> 리스너가 받아서 createWallet() 호출
     */
    public CashMember syncMember(MemberDto member) {
        boolean isNew = !cashMemberRepository.existsById(member.id());

        CashMember holder = cashMemberRepository.save(
                new CashMember(
                        member.id(),
                        member.username(),
                        "",
                        member.nickname(),
                        member.activityScore(),
                        member.createDate(),
                        member.modifyDate()
                ));

        // syncMember()는 가입뿐 아니라 수정 이벤트로도 호출된다. 확인하지 않으면 활동점수 바뀔 때마다 지갑을 만들려고 한다.
        if(isNew) {
            eventPublisher.publish(new CashMemberCreatedEvent(new CashMemberDto(holder)));
        }

        return holder;
    }
}
