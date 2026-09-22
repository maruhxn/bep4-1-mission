package com.back.shared.member.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MemberApiClient {
    private final RestClient restClient;

    public MemberApiClient(@Value("${custom.global.internalBackUrl}") String internalBackUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(internalBackUrl + "/api/v1/member")
                .build();
    }

    public String getRandomSecureTip() {
        return restClient.get()
                .uri("/randomSecureTip")
                .retrieve()
                .body(String.class);
    }
}
