package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashMember;
import com.back.bounded_context.cash.domain.Wallet;
import com.back.bounded_context.cash.out.CashMemberRepository;
import com.back.bounded_context.cash.out.WalletRepository;
import com.back.shared.cash.dto.CashMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashCreateWalletUseCase {
    private final CashMemberRepository cashMemberRepository;
    private final WalletRepository walletRepository;

    public Wallet createWallet(CashMemberDto member) {
        CashMember holder = cashMemberRepository.getReferenceById(member.id());
        var wallet = new Wallet(holder);

        return walletRepository.save(wallet);
    }
}
