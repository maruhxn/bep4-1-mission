package com.back.bounded_context.member.in;

import com.back.bounded_context.member.app.MemberFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberFacade memberFacade;

    @GetMapping("/randomSecureTip")
    public String getRandomSecureTip() {
        return memberFacade.getRandomSecureTip();
    }
}
