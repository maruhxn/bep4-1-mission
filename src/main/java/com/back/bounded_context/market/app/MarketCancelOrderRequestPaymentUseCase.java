package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.Order;
import com.back.bounded_context.market.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketCancelOrderRequestPaymentUseCase {
    private final OrderRepository orderRepository;

    public void cancelRequestPayment(int orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();

        order.cancelRequestPayment();
    }
}
