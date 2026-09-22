package com.back.bounded_context.payout.app;

import com.back.bounded_context.payout.domain.Payout;
import com.back.bounded_context.payout.domain.PayoutCandidateItem;
import com.back.global.dto.RsData;
import com.back.shared.market.dto.OrderDto;
import com.back.shared.member.dto.MemberDto;
import com.back.shared.payout.dto.PayoutMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayoutFacade {
    private final PayoutSyncMemberUseCase payoutSyncMemberUseCase;
    private final PayoutCreatePayoutUseCase payoutCreatePayoutUseCase;
    private final PayoutAddPayoutCandidateItemsUseCase payoutAddPayoutCandidateItemsUseCase;
    private final PayoutSupport payoutSupport;
    private final PayoutCollectPayoutItemsMoreUseCase payoutCollectPayoutItemsMoreUseCase;

    @Transactional
    public void syncMember(MemberDto member) {
        payoutSyncMemberUseCase.syncMember(member);
    }

    @Transactional
    public Payout createPayout(PayoutMemberDto payee) {
        return payoutCreatePayoutUseCase.createPayout(payee);
    }

    @Transactional
    public void addPayoutCandidateItems(OrderDto order) {
        payoutAddPayoutCandidateItemsUseCase.addPayoutCandidateItems(order);
    }

    @Transactional
    public RsData<Integer> collectPayoutItemsMore(int limit) {
        return payoutCollectPayoutItemsMoreUseCase.collectPayoutItemsMore(limit);
    }

    @Transactional(readOnly = true)
    public List<PayoutCandidateItem> findPayoutCandidateItems() {
        return payoutSupport.findPayoutCandidateItems();
    }
}