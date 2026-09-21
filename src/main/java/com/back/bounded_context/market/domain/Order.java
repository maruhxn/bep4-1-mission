package com.back.bounded_context.market.domain;

import com.back.global.jpa.BaseIdAndTime;
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
@Entity
@Table(name = "MARKET_ORDER")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseIdAndTime {
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> items = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private MarketMember buyer;
    private long price;
    private long salePrice;

    public Order(Cart cart) {
        this.buyer = cart.getBuyer();

        cart.getItems().forEach(item -> addItem(item.getProduct()));
    }

    public void addItem(Product product) {
        OrderItem orderItem = new OrderItem(this, product, product.getName(), product.getPrice(),
                product.getSalePrice());

        items.add(orderItem);

        price += product.getPrice();
        salePrice += product.getSalePrice();
    }
}
