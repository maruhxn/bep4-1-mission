package com.back.bounded_context.payout.out;

import com.back.bounded_context.payout.domain.Payout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayoutRepository extends JpaRepository<Payout, Integer> {
}
