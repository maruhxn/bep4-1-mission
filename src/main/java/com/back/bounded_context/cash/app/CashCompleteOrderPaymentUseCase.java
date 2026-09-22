package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashLogEventType;
import com.back.bounded_context.cash.domain.Wallet;
import com.back.global.event_publisher.EventPublisher;
import com.back.shared.cash.event.CashOrderPaymentFailedEvent;
import com.back.shared.cash.event.CashOrderPaymentSucceededEvent;
import com.back.shared.market.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashCompleteOrderPaymentUseCase {
    private final CashSupport cashSupport;
    private final EventPublisher eventPublisher;

    public void completeOrderPayment(OrderDto order, long pgPaymentAmount) {
        Wallet customerWallet = cashSupport.findWalletByHolderId(order.customerId()).orElseThrow();
        Wallet holdingWallet = cashSupport.findHoldingWallet().orElseThrow();

        if (pgPaymentAmount > 0) {
            customerWallet.credit(
                    pgPaymentAmount,
                    CashLogEventType.충전__PG결제_토스페이먼츠,
                    "Order",
                    order.id()
            );
        }

        boolean canPay = customerWallet.getBalance() >= order.salePrice();

        if (canPay) {
            customerWallet.debit(
                    order.salePrice(),
                    CashLogEventType.사용__주문결제,
                    "Order",
                    order.id()
            );

            holdingWallet.credit(
                    order.salePrice(),
                    CashLogEventType.임시보관__주문결제,
                    "Order",
                    order.id()
            );

            eventPublisher.publish(new CashOrderPaymentSucceededEvent(
                    order,
                    pgPaymentAmount
            ));
        } else {
            eventPublisher.publish(new CashOrderPaymentFailedEvent(
                    "400-1",
                    "충전은 완료했지만 %d번 주문을 결제완료처리 하기에는 예치금이 부족합니다.".formatted(order.id()),
                    order,
                    pgPaymentAmount,
                    order.salePrice() - customerWallet.getBalance()
            ));
        }
    }
}
