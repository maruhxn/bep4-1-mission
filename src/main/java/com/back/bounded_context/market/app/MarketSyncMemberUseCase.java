package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.MarketMember;
import com.back.bounded_context.market.out.MarketMemberRepository;
import com.back.global.event_publisher.EventPublisher;
import com.back.shared.market.dto.MarketMemberDto;
import com.back.shared.market.event.MarketMemberCreatedEvent;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketSyncMemberUseCase {
    private final MarketMemberRepository marketMemberRepository;
    private final EventPublisher eventPublisher;

    public MarketMember syncMember(MemberDto member) {
        boolean isNew = !marketMemberRepository.existsById(member.id());

        MarketMember marketMember = marketMemberRepository.save(new MarketMember(
                member.id(),
                member.username(),
                "",
                member.nickname(),
                member.activityScore(),
                member.createDate(),
                member.modifyDate()
        ));

        if (isNew) {
            eventPublisher.publish(new MarketMemberCreatedEvent(
                    new MarketMemberDto(marketMember)
            ));
        }

        return marketMember;
    }
}
