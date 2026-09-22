package com.back.bounded_context.payout.out;

import com.back.bounded_context.payout.domain.Payout;
import com.back.bounded_context.payout.domain.PayoutMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PayoutRepository extends JpaRepository<Payout, Integer> {
    Optional<Payout> findByPayeeAndPayoutDateIsNull(PayoutMember payee);
}
