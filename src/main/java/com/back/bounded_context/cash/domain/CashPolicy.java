package com.back.bounded_context.cash.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CashPolicy {
    public static int HOLDING_MEMBER_ID;

    @Value("${custom.global.holdingMemberId}")
    public void setHoldingMemberId(int id) {
        HOLDING_MEMBER_ID = id;
    }
}
