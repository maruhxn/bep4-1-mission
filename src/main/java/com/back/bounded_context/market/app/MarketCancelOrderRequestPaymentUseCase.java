package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.Order;
import com.back.bounded_context.market.out.OrderRepository;
import com.back.shared.cash.event.CashOrderPaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketCancelOrderRequestPaymentUseCase {
    private final OrderRepository orderRepository;

    public void handle(CashOrderPaymentFailedEvent event) {
        Order order = orderRepository.findById(event.order().id()).orElseThrow();

        order.cancelRequestPayment();
    }
}
