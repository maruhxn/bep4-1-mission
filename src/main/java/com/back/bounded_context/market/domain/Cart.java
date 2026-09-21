package com.back.bounded_context.market.domain;

import com.back.global.jpa.BaseManualIdAndTime;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "MARKET_CART")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseManualIdAndTime {
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private final List<CartItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private MarketMember buyer;

    private int itemsCount;

    public Cart(MarketMember buyer) {
        super(buyer.getId());
        this.buyer = buyer;
    }

    public boolean hasItems() {
        return itemsCount > 0;
    }

    public void addItem(Product product) {
        CartItem cartItem = new CartItem(this, product);

        this.getItems().add(cartItem);

        this.itemsCount++;
    }

    public void clearItems() {
        this.getItems().clear();
        this.itemsCount = 0;
    }
}
