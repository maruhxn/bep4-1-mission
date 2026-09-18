package com.back.bounded_context.post.domain;

import com.back.shared.member.domain.ReplicaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "POST_MEMBER")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostMember extends ReplicaMember {

    public PostMember(String username, String password, String nickname) {
        super(username, password, nickname);
    }
}
