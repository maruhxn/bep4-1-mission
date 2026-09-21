package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.Order;
import com.back.bounded_context.market.out.OrderRepository;
import com.back.shared.cash.event.CashOrderPaymentSucceededEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketCompleteOrderPaymentUseCase {
    private final OrderRepository orderRepository;

    public void handle(CashOrderPaymentSucceededEvent event) {
        Order order = orderRepository.findById(event.order().id()).orElseThrow();

        order.completePayment();
    }
}