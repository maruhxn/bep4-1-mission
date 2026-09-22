package com.back.bounded_context.cash.domain;

import com.back.shared.cash.dto.CashMemberDto;
import com.back.shared.member.domain.ReplicaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CASH_MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CashMember extends ReplicaMember {
    public CashMember(
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

    public CashMemberDto toDto() {
        return new CashMemberDto(
                getId(),
                getCreateDate(),
                getModifyDate(),
                getUsername(),
                getNickname(),
                getActivityScore()
        );
    }
}