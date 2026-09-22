package com.back.shared.payout.dto;

import java.time.LocalDateTime;

public record PayoutMemberDto(
        int id,
        String username,
        String nickname,
        int activityScore,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
}