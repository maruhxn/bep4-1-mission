package com.back.bounded_context.market.app;


import com.back.bounded_context.market.domain.Cart;
import com.back.bounded_context.market.domain.MarketMember;
import com.back.bounded_context.market.domain.Product;
import com.back.bounded_context.market.out.CartRepository;
import com.back.bounded_context.market.out.MarketMemberRepository;
import com.back.bounded_context.market.out.OrderRepository;
import com.back.bounded_context.market.out.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketSupport {
    private final ProductRepository productRepository;
    private final MarketMemberRepository marketMemberRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public long countProducts() {
        return productRepository.count();
    }

    public Optional<MarketMember> findMemberByUsername(String username) {
        return marketMemberRepository.findByUsername(username);
    }

    public Optional<Cart> findCartByBuyer(MarketMember buyer) {
        return cartRepository.findByBuyer(buyer);
    }

    public Optional<Product> findProductById(int id) {
        return productRepository.findById(id);
    }

    public long countOrders() {
        return orderRepository.count();
    }
}