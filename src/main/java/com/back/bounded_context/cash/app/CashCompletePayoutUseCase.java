package com.back.bounded_context.cash.app;

import com.back.bounded_context.cash.domain.CashLogEventType;
import com.back.bounded_context.cash.domain.Wallet;
import com.back.shared.payout.dto.PayoutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashCompletePayoutUseCase {
    private final CashSupport cashSupport;

    /**
     * holding 지갑에서 차감, 수령인 지갑에 입금, CashLog 기록
     * <p>
     * 수령인이 system이면 수수료, 아니면 판매대급으로 CashLog의 종류가 나뉜다.
     */
    public void completePayout(PayoutDto payout) {
        Wallet holdingWallet = cashSupport.findHoldingWallet().get();
        Wallet payeeWallet = cashSupport.findWalletByHolderId(payout.payeeId()).get();

        holdingWallet.debit(
                payout.amount(),
                payout.isPayeeSystem() ? CashLogEventType.정산지급__상품판매_수수료 : CashLogEventType.정산지급__상품판매_대금,
                payout.getModelTypeCode(),
                payout.id()
        );

        payeeWallet.credit(
                payout.amount(),
                payout.isPayeeSystem() ? CashLogEventType.정산수령__상품판매_수수료 : CashLogEventType.정산수령__상품판매_대금,
                payout.getModelTypeCode(),
                payout.id()
        );
    }
}