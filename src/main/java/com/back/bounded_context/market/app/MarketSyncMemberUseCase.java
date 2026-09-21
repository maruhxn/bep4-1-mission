package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.MarketMember;
import com.back.bounded_context.market.out.MarketMemberRepository;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketSyncMemberUseCase {
    private final MarketMemberRepository marketMemberRepository;

    public MarketMember syncMember(MemberDto member) {
        MarketMember marketMember = new MarketMember(
                member.id(),
                member.username(),
                "",
                member.nickname(),
                member.activityScore(),
                member.createDate(),
                member.modifyDate()
        );

        return marketMemberRepository.save(marketMember);
    }
}
