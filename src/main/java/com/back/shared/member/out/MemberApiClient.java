package com.back.shared.member.out;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MemberApiClient {
    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://localhost:8080/api/v1/members")
            .build();

    public String getRandomSecureTip() {
        return restClient.get()
                .uri("/randomSecureTip")
                .retrieve()
                .body(String.class);
    }
}
