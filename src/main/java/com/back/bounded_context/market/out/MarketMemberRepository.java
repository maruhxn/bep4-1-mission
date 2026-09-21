package com.back.bounded_context.market.out;

import com.back.bounded_context.market.domain.MarketMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarketMemberRepository extends JpaRepository<MarketMember, Integer> {
    Optional<MarketMember> findByUsername(String username);
}
