package com.back.bounded_context.payout.domain;

import com.back.global.jpa.BaseIdAndTime;
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

/**
 * 정산서
 */
@Getter
@Table(name = "PAYOUT_PAYOUT")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payout extends BaseIdAndTime {

    @OneToMany(mappedBy = "payout", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PayoutItem> items = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private PayoutMember payee;
    private LocalDateTime payoutDate;
    private long amount;

    public Payout(PayoutMember payee) {
        this.payee = payee;
    }

    public PayoutItem addItem(
            PayoutEventType eventType,
            String relTypeCode,
            int relId,
            LocalDateTime payDate,
            PayoutMember payer,
            PayoutMember payee,
            long amount
    ) {
        PayoutItem payoutItem = new PayoutItem(
                this, eventType, relTypeCode, relId, payDate, payer, payee, amount
        );

        items.add(payoutItem);

        this.amount += amount;

        return payoutItem;
    }
}
