package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.Cart;
import com.back.bounded_context.market.domain.Order;
import com.back.bounded_context.market.out.OrderRepository;
import com.back.global.dto.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketCreateOrderUseCase {
    private final OrderRepository orderRepository;

    public RsData<Order> createOrder(Cart cart) {
        Order order = orderRepository.save(new Order(cart));

        cart.clearItems();

        return new RsData<>("201-1", "%d번 주문이 생성되었습니다.".formatted(order.getId()), order);
    }
}
