package com.back.bounded_context.market.domain;

import com.back.global.jpa.BaseIdAndTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MARKET_CART_ITEM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseIdAndTime {
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public CartItem(Cart cart, Product product) {
        this.cart = cart;
        this.product = product;
    }
}