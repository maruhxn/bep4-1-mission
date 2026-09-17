package com.back.entity;

import com.back.shared.BaseIdAndTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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