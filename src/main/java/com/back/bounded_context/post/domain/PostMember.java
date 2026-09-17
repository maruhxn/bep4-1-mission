package com.back.bounded_context.post.domain;

import com.back.global.jpa.BaseIdAndTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "POST_MEMBER")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMember extends BaseIdAndTime {

    @Column(unique = true)
    private String username;

    private String password;

    private String nickname;

    private int activityScore;
}
