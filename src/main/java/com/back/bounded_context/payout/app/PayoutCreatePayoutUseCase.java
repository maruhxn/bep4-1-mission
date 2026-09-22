package com.back.bounded_context.payout.app;

import com.back.bounded_context.payout.domain.Payout;
import com.back.bounded_context.payout.domain.PayoutMember;
import com.back.bounded_context.payout.out.PayoutMemberRepository;
import com.back.bounded_context.payout.out.PayoutRepository;
import com.back.shared.payout.dto.PayoutMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayoutCreatePayoutUseCase {
    private final PayoutRepository payoutRepository;
    private final PayoutMemberRepository payoutMemberRepository;

    public Payout createPayout(PayoutMemberDto payee) {
        PayoutMember _payee = payoutMemberRepository.getReferenceById(payee.id());

        return payoutRepository.save(new Payout(_payee));
    }
}