package com.back.bounded_context.market.domain;

import com.back.shared.member.domain.ReplicaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MARKET_MEMBER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketMember extends ReplicaMember {
    public MarketMember(
            int id,
            String username,
            String password,
            String nickname,
            int activityScore,
            LocalDateTime createDate,
            LocalDateTime modifyDate
    ) {
        super(id, username, password, nickname, activityScore, createDate, modifyDate);
    }
}
