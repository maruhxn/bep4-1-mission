package com.back.bounded_context.market.in.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmPaymentByTossPaymentsReqBody(
        @NotBlank String paymentKey,
        @NotBlank String orderId,
        @NotNull int amount
) {
}