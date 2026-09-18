package com.back.shared.member.domain;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Source로부터 그대로 복제해야 하기 때문에 Auto Increment + Auditing 기능은 제거
 */
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ReplicaMember extends BaseMember {
    @Id
    private int id;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    public ReplicaMember(
            int id,
            String username,
            String password,
            String nickname,
            int activityScore,
            LocalDateTime createDate,
            LocalDateTime modifyDate
    ) {
        super(username, password, nickname, activityScore);
        this.id = id;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }
}
