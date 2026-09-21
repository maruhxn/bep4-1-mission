package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashMember;
import com.back.bounded_context.cash.domain.CashPolicy;
import com.back.bounded_context.cash.domain.Wallet;
import com.back.bounded_context.cash.out.CashMemberRepository;
import com.back.bounded_context.cash.out.WalletRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashSupport {
    private final CashMemberRepository cashMemberRepository;
    private final WalletRepository walletRepository;

    public Optional<CashMember> findMemberByUsername(String username) {
        return cashMemberRepository.findByUsername(username);
    }

    public Optional<Wallet> findWalletByHolder(CashMember holder) {
        return walletRepository.findByHolder(holder);
    }

    public Optional<Wallet> findWalletByHolderId(int holderId) {
        return walletRepository.findByHolderId(holderId);
    }

    public Optional<Wallet> findHoldingWallet() {
        return walletRepository.findByHolderId(CashPolicy.HOLDING_MEMBER_ID);
    }
}