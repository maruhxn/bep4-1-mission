package com.back.bounded_context.payout.domain;

import com.back.global.jpa.BaseIdAndTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PAYOUT_PAYOUT_CANDIDATE_ITEM")
@NoArgsConstructor
@Getter
public class PayoutCandidateItem extends BaseIdAndTime {
    String relTypeCode;
    @Enumerated(EnumType.STRING)
    private PayoutEventType eventType;
    private int relId;
    private LocalDateTime paymentDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private PayoutMember payer;
    @ManyToOne(fetch = FetchType.LAZY)
    private PayoutMember payee;
    private long amount;
    @OneToOne(fetch = FetchType.LAZY)
    @Setter
    private PayoutItem payoutItem;

    public PayoutCandidateItem(PayoutEventType eventType, String relTypeCode, int relId, LocalDateTime paymentDate,
                               PayoutMember payer, PayoutMember payee, long amount) {
        this.eventType = eventType;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.paymentDate = paymentDate;
        this.payer = payer;
        this.payee = payee;
        this.amount = amount;
    }
}