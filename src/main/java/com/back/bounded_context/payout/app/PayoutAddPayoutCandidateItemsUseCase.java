package com.back.bounded_context.payout.app;

import com.back.bounded_context.payout.domain.PayoutCandidateItem;
import com.back.bounded_context.payout.domain.PayoutEventType;
import com.back.bounded_context.payout.domain.PayoutMember;
import com.back.bounded_context.payout.out.PayoutCandidateItemRepository;
import com.back.shared.market.dto.OrderDto;
import com.back.shared.market.dto.OrderItemDto;
import com.back.shared.market.out.MarketApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayoutAddPayoutCandidateItemsUseCase {
    private final MarketApiClient marketApiClient;
    private final PayoutSupport payoutSupport;
    private final PayoutCandidateItemRepository payoutCandidateItemRepository;

    public void addPayoutCandidateItems(OrderDto order) {
        List<OrderItemDto> items = marketApiClient.getOrderItems(order.id());

        items.forEach(orderItem -> makePayoutCandidateItems(order, orderItem));
    }

    /**
     * 주문 품목 1개에서 정산 후보 2건을 만든다.
     * - 수수료: 구매자(buyer) -> system
     * - 판매대금: 구매자(buyer) -> 판매자(seller)
     * +) 바로 정산서에 담지 않고 후보로 쌓아두는 이유 : 결제 직후에는 환불될 수 있다. 대기 기간이 지난 것만 정산 대상이 된다
     */
    private void makePayoutCandidateItems(
            OrderDto order,
            OrderItemDto orderItem
    ) {
        PayoutMember system = payoutSupport.findSystemMember().get();
        PayoutMember buyer = payoutSupport.findMemberById(orderItem.buyerId()).get();
        PayoutMember seller = payoutSupport.findMemberById(orderItem.sellerId()).get();

        makePayoutCandidateItem(
                PayoutEventType.정산__상품판매_수수료,
                orderItem.getModelTypeCode(),
                orderItem.id(),
                order.paymentDate(),
                buyer,
                system,
                orderItem.payoutFee()
        );

        makePayoutCandidateItem(
                PayoutEventType.정산__상품판매_대금,
                orderItem.getModelTypeCode(),
                orderItem.id(),
                order.paymentDate(),
                buyer,
                seller,
                orderItem.salePriceWithoutFee()
        );
    }

    private void makePayoutCandidateItem(
            PayoutEventType eventType,
            String relTypeCode,
            int relId,
            LocalDateTime paymentDate,
            PayoutMember payer,
            PayoutMember payee,
            long amount
    ) {
        payoutCandidateItemRepository.save(new PayoutCandidateItem(
                eventType,
                relTypeCode,
                relId,
                paymentDate,
                payer,
                payee,
                amount
        ));
    }
}