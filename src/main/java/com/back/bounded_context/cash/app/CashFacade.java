package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashMember;
import com.back.bounded_context.cash.domain.Wallet;
import com.back.shared.cash.dto.CashMemberDto;
import com.back.shared.market.event.MarketOrderPaymentRequestedEvent;
import com.back.shared.member.dto.MemberDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CashFacade {
    private final CashCreateWalletUseCase cashCreateWalletUseCase;
    private final CashSyncMemberUseCase cashSyncMemberUseCase;
    private final CashSupport cashSupport;
    private final CashCompleteOrderPaymentUseCase cashCompleteOrderPaymentUseCase;

    @Transactional
    public CashMember syncMember(MemberDto member) {
        return cashSyncMemberUseCase.syncMember(member);
    }

    @Transactional
    public Wallet createWallet(CashMemberDto holder) {
        return cashCreateWalletUseCase.createWallet(holder);
    }

    @Transactional(readOnly = true)
    public Optional<CashMember> findMemberByUsername(String username) {
        return cashSupport.findMemberByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Wallet> findWalletByHolder(CashMember holder) {
        return cashSupport.findWalletByHolder(holder);
    }

    @Transactional
    public void handle(MarketOrderPaymentRequestedEvent event) {
        cashCompleteOrderPaymentUseCase.handle(event);
    }
}