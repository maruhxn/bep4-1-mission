package com.back.bounded_context.payout.app;

import com.back.bounded_context.payout.domain.Payout;
import com.back.bounded_context.payout.out.PayoutRepository;
import com.back.global.dto.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayoutCompletePayoutsMoreUseCase {
    private final PayoutRepository payoutRepository;

    /**
     * 정산서(Payout)에 돈이 모였으니, 실제로 돈을 옮긴다.
     */
    public RsData<Integer> completePayoutsMore(int limit) {
        List<Payout> activePayouts = findActivePayouts(limit);

        if (activePayouts.isEmpty())
            return new RsData<>("200-1", "더 이상 정산할 정산내역이 없습니다.", 0);

        activePayouts.forEach(Payout::completePayout);

        return new RsData<>(
                "201-1",
                "%d건의 정산이 처리되었습니다.".formatted(activePayouts.size()),
                activePayouts.size()
        );
    }

    /**
     * 아직 집행하지 않았고 금액이 있는 정산서를 limit 건씩 조회
     */
    private List<Payout> findActivePayouts(int limit) {
        return payoutRepository.findByPayoutDateIsNullAndAmountGreaterThanOrderByIdAsc(0, PageRequest.of(0, limit));
    }
}