package com.back.bounded_context.cash.domain;

import com.back.global.jpa.BaseEntity;
import com.back.global.jpa.BaseManualIdAndTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "CASH_WALLET")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet extends BaseManualIdAndTime {
    @ManyToOne(fetch = FetchType.LAZY)
    private CashMember holder;

    private long balance;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CashLog> cashLogs = new ArrayList<>();

    public Wallet(CashMember holder) {
        super(holder.getId());
        this.holder = holder;
    }

    // ========================================================

    public boolean hasBalance() {
        return balance > 0;
    }

    public void credit(long amount, CashLogEventType eventType, String relTypeCode, int relId) {
        balance += amount;

        addCashLog(amount, eventType, relTypeCode, relId);
    }

    public void credit(long amount, CashLogEventType eventType, BaseEntity rel) {
        credit(amount, eventType, rel.getModelTypeCode(), rel.getId());
    }

    public void credit(long amount, CashLogEventType eventType) {
        credit(amount, eventType, holder);
    }

    public void debit(long amount, CashLogEventType eventType, String relTypeCode, int relId) {
        balance -= amount;

        addCashLog(-amount, eventType, relTypeCode, relId);
    }

    public void debit(long amount, CashLogEventType eventType, BaseEntity rel) {
        debit(amount, eventType, rel.getModelTypeCode(), rel.getId());
    }

    public void debit(long amount, CashLogEventType eventType) {
        debit(amount, eventType, holder);
    }

    private CashLog addCashLog(long amount, CashLogEventType eventType, String relTypeCode, int relId) {
        CashLog cashLog = new CashLog(
                eventType,
                relTypeCode,
                relId,
                holder,
                this,
                amount,
                balance
        );

        cashLogs.add(cashLog);

        return cashLog;
    }
}
