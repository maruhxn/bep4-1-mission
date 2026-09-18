package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashMember;
import com.back.bounded_context.cash.domain.Wallet;
import com.back.bounded_context.cash.out.CashMemberRepository;
import com.back.bounded_context.cash.out.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}