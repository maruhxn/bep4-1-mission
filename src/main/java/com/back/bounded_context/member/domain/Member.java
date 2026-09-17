package com.back.bounded_context.member.domain;

import com.back.global.jpa.BaseIdAndTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "MEMBER_MEMBER")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseIdAndTime {
    @Column(unique = true)
    private String username;

    private String password;

    private String nickname;

    private int activityScore;

    public Member(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }

    // ============================================

    public int increaseActivityScore(int amount) {
        return this.activityScore += amount;
    }
}