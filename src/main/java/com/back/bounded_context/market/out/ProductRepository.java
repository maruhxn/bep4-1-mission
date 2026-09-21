package com.back.bounded_context.market.out;

import com.back.bounded_context.market.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
