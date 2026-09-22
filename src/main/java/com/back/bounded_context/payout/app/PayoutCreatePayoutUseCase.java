package com.back.bounded_context.payout.app;

import com.back.shared.payout.dto.PayoutMemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayoutCreatePayoutUseCase {
    public void createPayout(PayoutMemberDto payee) {
        log.debug("createPayout.payee: {}", payee.id());
    }
}