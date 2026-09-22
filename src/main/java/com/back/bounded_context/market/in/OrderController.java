package com.back.bounded_context.market.in;

import com.back.bounded_context.market.app.MarketFacade;
import com.back.bounded_context.market.domain.Order;
import com.back.bounded_context.market.domain.OrderItem;
import com.back.bounded_context.market.in.dto.ConfirmPaymentByTossPaymentsReqBody;
import com.back.global.dto.RsData;
import com.back.global.exception.DomainException;
import com.back.shared.cash.out.CashApiClient;
import com.back.shared.market.dto.OrderItemDto;
import com.back.shared.market.out.TossPaymentsService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/market/orders")
@RequiredArgsConstructor
public class OrderController {
    private final MarketFacade marketFacade;
    private final TossPaymentsService tossPaymentsService;
    private final CashApiClient cashApiClient;

    @CrossOrigin(
            origins = {
                    "https://cdpn.io",
                    "https://codepen.io"
            },
            allowedHeaders = "*",
            methods = {RequestMethod.POST}
    )
    @PostMapping("/{id}/payment/confirm/by/tossPayments")
    @Transactional
    public RsData<Void> confirmPaymentByTossPayments(
            @PathVariable int id,
            @Valid @RequestBody ConfirmPaymentByTossPaymentsReqBody reqBody
    ) {
        Order order = marketFacade.findOrderById(id).orElseThrow();

        validate(reqBody, order);

        tossPaymentsService.confirmCardPayment(
                reqBody.paymentKey(),
                reqBody.orderId(),
                reqBody.amount()
        );

        marketFacade.requestPayment(order, reqBody.amount());

        return new RsData<>("202-1", "결제 프로세스가 시작되었습니다.");
    }

    private void validate(ConfirmPaymentByTossPaymentsReqBody reqBody, Order order) {
        if (order.isCanceled()) {
            throw new DomainException("400-1", "이미 취소된 주문입니다.");
        }

        if (order.isPaymentInProgress()) {
            throw new DomainException("400-2", "이미 결제 진행중인 주문입니다.");
        }

        if (order.isPaid()) {
            throw new DomainException("400-3", "이미 결제된 주문입니다.");
        }

        long walletBalance = cashApiClient.getBalanceByHolderId(order.getBuyer().getId());

        if (order.getSalePrice() > walletBalance + reqBody.amount()) {
            throw new DomainException("400-4", "결제를 완료하기에 결제 금액이 부족합니다.");
        }

        if (order.getId() != Integer.parseInt(reqBody.orderId().split("-", 3)[1])) {
            throw new DomainException("400-5", "주문번호가 일치하지 않습니다.");
        }
    }

    @GetMapping("/{id}/items")
    @Transactional(readOnly = true)
    public List<OrderItemDto> getItems(@PathVariable int id) {
        Order order = marketFacade.findOrderById(id).get();
        return order.getItems()
                .stream()
                .map(OrderItem::toDto)
                .toList();
    }
}
