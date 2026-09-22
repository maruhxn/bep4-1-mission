package com.back.bounded_context.cash.domain;

import com.back.global.jpa.BaseEntity;
import com.back.global.jpa.BaseManualIdAndTime;
import com.back.shared.cash.dto.WalletDto;
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
@Table(name = "CASH_WALLET")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet extends BaseManualIdAndTime {
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CashLog> cashLogs = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    private CashMember holder;
    private long balance;

    public Wallet(CashMember holder) {
        super(holder.getId());
        this.holder = holder;
    }

    public WalletDto toDto() {
        return new WalletDto(
                getId(),
                getCreateDate(),
                getModifyDate(),
                holder.getId(),
                holder.getUsername(),
                balance
        );
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
