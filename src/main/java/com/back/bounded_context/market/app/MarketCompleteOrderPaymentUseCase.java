package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.Order;
import com.back.bounded_context.market.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketCompleteOrderPaymentUseCase {
    private final OrderRepository orderRepository;

    public void completePayment(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        order.completePayment();
    }
}