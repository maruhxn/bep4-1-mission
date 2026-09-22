package com.back.bounded_context.payout.domain;

import com.back.global.jpa.BaseIdAndTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

/**
 * 정산 항목
 */
@Entity
@Table(name = "PAYOUT_PAYOUT_ITEM")
@NoArgsConstructor
public class PayoutItem extends BaseIdAndTime {
    String relTypeCode;
    @ManyToOne(fetch = FetchType.LAZY)
    private Payout payout;
    @Enumerated(EnumType.STRING)
    private PayoutEventType eventType;
    private int relId;
    private LocalDateTime paymentDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private PayoutMember payer;
    @ManyToOne(fetch = FetchType.LAZY)
    private PayoutMember payee;
    private long amount;

    public PayoutItem(Payout payout, PayoutEventType eventType, String relTypeCode, int relId, LocalDateTime payDate,
                      PayoutMember payer, PayoutMember payee, long amount) {
        this.payout = payout;
        this.eventType = eventType;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.paymentDate = payDate;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
    }
}