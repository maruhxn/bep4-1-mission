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
@Table(name = "MARKET_PRODUCT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseIdAndTime {

    @ManyToOne(fetch = FetchType.LAZY)
    private MarketMember seller;

    private String sourceTypeCode;

    private int sourceId;

    private String name;

    private String description;

    private long price;

    private long salePrice;

    public Product(MarketMember seller, String sourceTypeCode, int sourceId, String name, String description,
                   long price,
                   long salePrice) {
        this.seller = seller;
        this.sourceTypeCode = sourceTypeCode;
        this.sourceId = sourceId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
    }
}
