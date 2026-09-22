package com.back.bounded_context.payout.app;

import com.back.bounded_context.payout.domain.*;
import com.back.bounded_context.payout.out.PayoutCandidateItemRepository;
import com.back.bounded_context.payout.out.PayoutRepository;
import com.back.global.dto.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayoutCollectPayoutItemsMoreUseCase {
    private final PayoutRepository payoutRepository;
    private final PayoutCandidateItemRepository payoutCandidateItemRepository;

    /**
     * 한 번에 전부 처리하지 않고 limit 건씩 나눠서 처리.
     * 후보가 수십만 건이면 한 트랜잭션으로 처리 불가
     */
    public RsData<Integer> collectPayoutItemsMore(int limit) {
        // 1. 대상 조회
        List<PayoutCandidateItem> payoutReadyCandidateItems = findPayoutReadyCandidateItems(limit);

        if (payoutReadyCandidateItems.isEmpty())
            return new RsData<>("200-1", "더 이상 정산에 추가할 항목이 없습니다.", 0);

        payoutReadyCandidateItems.stream()
                // 정산 후보를 수령인별로 묶고 열려있는 정산서에 항목을 추가. 처리한 후보에는 생성한 정산 항목을 연결
                .collect(Collectors.groupingBy(PayoutCandidateItem::getPayee))
                .forEach((payee, candidateItems) -> {
                    // 처리된 후보는 PayoutItem으로 Payout 채워짐.
                    Payout payout = findActiveByPayee(payee).get();

                    candidateItems.forEach(item -> {
                        PayoutItem payoutItem = payout.addItem(
                                item.getEventType(),
                                item.getRelTypeCode(),
                                item.getRelId(),
                                item.getPaymentDate(),
                                item.getPayer(),
                                item.getPayee(),
                                item.getAmount()
                        );

                        item.setPayoutItem(payoutItem);
                    });
                });


        return new RsData<>(
                "201-1",
                "%d건의 정산데이터가 생성되었습니다.".formatted(payoutReadyCandidateItems.size()),
                payoutReadyCandidateItems.size()
        );
    }

    /**
     * 아직 정산 항목이 없고, 결제 후 대기 기간(14일)이 지난 후보만 limit 건씩 조회
     */
    private List<PayoutCandidateItem> findPayoutReadyCandidateItems(int limit) {
        LocalDateTime daysAgo = LocalDateTime
                .now()
                .minusDays(PayoutPolicy.PAYOUT_READY_WAITING_DAYS)
                .toLocalDate()
                .atStartOfDay();

        return payoutCandidateItemRepository.findByPayoutItemIsNullAndPaymentDateBeforeOrderByPayeeAscIdAsc(
                daysAgo,
                PageRequest.of(0, limit)
        );
    }

    private Optional<Payout> findActiveByPayee(PayoutMember payee) {
        return payoutRepository.findByPayeeAndPayoutDateIsNull(payee);
    }
}