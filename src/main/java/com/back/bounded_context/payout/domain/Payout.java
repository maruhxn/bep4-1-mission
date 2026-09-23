package com.back.bounded_context.payout.domain;

import com.back.global.jpa.BaseIdAndTime;
import com.back.shared.payout.dto.PayoutDto;
import com.back.shared.payout.event.PayoutCompletedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public void completePayout() {
        this.payoutDate = LocalDateTime.now();

        publishEvent(
                new PayoutCompletedEvent(
                        toDto()
                )
        );
    }

    public PayoutDto toDto() {
        return new PayoutDto(
                getId(),
                getCreateDate(),
                getModifyDate(),
                payee.getId(),
                payee.getNickname(),
                payoutDate,
                amount,
                payee.isSystem()
        );
    }
}
