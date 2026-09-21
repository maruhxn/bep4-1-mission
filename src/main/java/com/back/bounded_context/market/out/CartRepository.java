package com.back.bounded_context.market.out;

import com.back.bounded_context.market.domain.Cart;
import com.back.bounded_context.market.domain.MarketMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByBuyer(MarketMember buyer);
}
