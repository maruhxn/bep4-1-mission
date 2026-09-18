package com.back.bounded_context.cash.out;

import com.back.bounded_context.cash.domain.CashMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashMemberRepository extends JpaRepository<CashMember, Integer> {
    Optional<CashMember> findByUsername(String username);
}