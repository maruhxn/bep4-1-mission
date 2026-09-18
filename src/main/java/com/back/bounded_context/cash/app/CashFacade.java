package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashMember;
import com.back.bounded_context.cash.domain.Wallet;
import com.back.bounded_context.cash.out.CashMemberRepository;
import com.back.bounded_context.cash.out.WalletRepository;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CashFacade {
    private final CashMemberRepository cashMemberRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public CashMember syncMember(MemberDto member) {
        CashMember _member = new CashMember(
                member.id(),
                member.username(),
                "",
                member.nickname(),
                member.activityScore(),
                member.createDate(),
                member.modifyDate()
        );

        return cashMemberRepository.save(_member);
    }

    @Transactional
    public Wallet createWallet(CashMember holder) {
        var wallet = new Wallet(holder);

        return walletRepository.save(wallet);
    }
}