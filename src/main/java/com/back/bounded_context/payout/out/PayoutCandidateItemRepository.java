package com.back.bounded_context.payout.out;

import com.back.bounded_context.payout.domain.PayoutCandidateItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PayoutCandidateItemRepository extends JpaRepository<PayoutCandidateItem, Integer> {
    List<PayoutCandidateItem> findByPayoutItemIsNullAndPaymentDateBeforeOrderByPayeeAscIdAsc(
            LocalDateTime paymentDate,
            Pageable pageable
    );
}