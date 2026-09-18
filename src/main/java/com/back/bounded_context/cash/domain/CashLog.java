package com.back.bounded_context.cash.domain;

import com.back.global.jpa.BaseIdAndTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "CASH_CASH_LOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CashLog extends BaseIdAndTime {
    @Enumerated(EnumType.STRING)
    private CashLogEventType eventType;

    /**
     * 관련 대상(relTypeCode, relId)
     * -> 나중에 이 돈이 어느 주문 때문에 움직였는지 추적하기 위함
     */
    private String relTypeCode;

    private int relId;

    @ManyToOne(fetch = FetchType.LAZY)
    private CashMember holder;

    @ManyToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    private long amount;

    private long balance;

    public CashLog(CashLogEventType eventType, String relTypeCode, int relId, CashMember holder, Wallet wallet, long amount, long balance) {
        this.eventType = eventType;
        this.relTypeCode = relTypeCode;
        this.relId = relId;
        this.holder = holder;
        this.wallet = wallet;
        this.amount = amount;
        this.balance = balance;
    }
}
