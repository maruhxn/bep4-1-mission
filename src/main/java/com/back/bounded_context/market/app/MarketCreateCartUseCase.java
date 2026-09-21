package com.back.bounded_context.market.app;

import com.back.bounded_context.market.domain.Cart;
import com.back.bounded_context.market.domain.MarketMember;
import com.back.bounded_context.market.out.CartRepository;
import com.back.bounded_context.market.out.MarketMemberRepository;
import com.back.global.dto.RsData;
import com.back.shared.market.dto.MarketMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketCreateCartUseCase {
    private final MarketMemberRepository marketMemberRepository;
    private final CartRepository cartRepository;

    public RsData<Cart> createCart(MarketMemberDto buyerDto) {
        MarketMember buyer = marketMemberRepository.getReferenceById(buyerDto.id());

        var cart = new Cart(buyer);
        cartRepository.save(cart);

        return new RsData<>("201-1", "장바구니가 생성되었습니다.", cart);
    }
}
