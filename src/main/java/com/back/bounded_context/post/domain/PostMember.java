package com.back.bounded_context.post.domain;

import com.back.shared.member.domain.ReplicaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Table(name = "POST_MEMBER")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMember extends ReplicaMember {

    public PostMember(int id, String username, String password, String nickname, LocalDateTime createDate, LocalDateTime modifyDate)  {
        super(id, username, password, nickname, createDate, modifyDate);
    }
}
