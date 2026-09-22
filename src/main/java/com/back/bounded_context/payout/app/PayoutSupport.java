package com.back.bounded_context.payout.app;

import com.back.bounded_context.payout.domain.PayoutCandidateItem;
import com.back.bounded_context.payout.domain.PayoutMember;
import com.back.bounded_context.payout.out.PayoutCandidateItemRepository;
import com.back.bounded_context.payout.out.PayoutMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PayoutSupport {
    private final PayoutMemberRepository payoutMemberRepository;
    private final PayoutCandidateItemRepository payoutCandidateItemRepository;

    public Optional<PayoutMember> findSystemMember() {
        return payoutMemberRepository.findByUsername("system");
    }

    public Optional<PayoutMember> findMemberById(int id) {
        return payoutMemberRepository.findById(id);
    }

    public List<PayoutCandidateItem> findPayoutCandidateItems() {
        return payoutCandidateItemRepository.findAll();
    }
}