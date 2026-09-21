package com.back.shared.market.dto;

import com.back.bounded_context.market.domain.MarketMember;
import java.time.LocalDateTime;

public record MarketMemberDto(
        int id,
        String username,
        String nickname,
        int activityScore,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
    public MarketMemberDto(MarketMember member) {
        this(
                member.getId(),
                member.getUsername(),
                member.getNickname(),
                member.getActivityScore(),
                member.getCreateDate(),
                member.getModifyDate()
        );
    }
}