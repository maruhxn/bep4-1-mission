package com.back.bounded_context.market.domain;

import com.back.global.jpa.BaseIdAndTime;
import com.back.shared.market.dto.OrderDto;
import com.back.shared.market.event.MarketOrderPaymentRequestedEvent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
    private LocalDateTime requestPaymentDate;
    private LocalDateTime paymentDate;

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

    public void requestPayment(long pgPaymentAmount) {
        this.requestPaymentDate = LocalDateTime.now();

        publishEvent(new MarketOrderPaymentRequestedEvent(
                new OrderDto(this),
                pgPaymentAmount
        ));
    }

    public void completePayment() {
        paymentDate = LocalDateTime.now();
    }

    public void cancelRequestPayment() {
        requestPaymentDate = null;
    }

    public boolean isPaid() {
        return paymentDate != null;
    }
}
