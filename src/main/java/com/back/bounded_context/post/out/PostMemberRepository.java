package com.back.bounded_context.post.out;

import com.back.bounded_context.post.domain.PostMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMemberRepository extends JpaRepository<PostMember, Integer> {
}
