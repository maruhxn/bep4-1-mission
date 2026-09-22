package com.back.bounded_context.payout.domain;

import com.back.shared.member.domain.ReplicaMember;
import com.back.shared.payout.dto.PayoutMemberDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PAYOUT_MEMBER")
@Getter
@NoArgsConstructor
public class PayoutMember extends ReplicaMember {
    public PayoutMember(
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

    public PayoutMemberDto toDto() {
        return new PayoutMemberDto(
                getId(),
                getUsername(),
                getNickname(),
                getActivityScore(),
                getCreateDate(),
                getModifyDate()
        );
    }
}