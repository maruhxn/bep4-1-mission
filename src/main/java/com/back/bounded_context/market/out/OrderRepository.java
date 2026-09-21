package com.back.bounded_context.market.out;

import com.back.bounded_context.market.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
