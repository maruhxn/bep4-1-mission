package com.back.shared.market.dto;

import java.time.LocalDateTime;

public record MarketMemberDto(
        int id,
        String username,
        String nickname,
        int activityScore,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
}