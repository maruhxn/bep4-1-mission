package com.back.integration;

import com.back.bounded_context.member.app.MemberFacade;
import com.back.bounded_context.member.domain.Member;
import com.back.bounded_context.member.out.MemberRepository;
import com.back.bounded_context.post.app.PostFacade;
import com.back.bounded_context.post.domain.PostMember;
import com.back.bounded_context.post.out.PostMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 회원 가입 시 post 컨텍스트에 복제본(PostMember)이 생성되고,
 * 활동 점수 변경(MemberModifiedEvent) 시 복제본 점수도 동기화되는지 검증한다.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:integration_test;MODE=MySQL;DB_CLOSE_DELAY=-1"
)
class MemberReplicaSyncIntegrationTest {

    @Autowired
    private MemberFacade memberFacade;
    @Autowired
    private PostFacade postFacade;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostMemberRepository postMemberRepository;

    @Test
    void 회원가입_시_복제본이_생성되고_원본과_일치한다() {
        String username = "syncUser1";
        memberFacade.join(username, "pw1234!", "동기화유저1");

        Member source = memberRepository.findByUsername(username).orElseThrow();
        PostMember replica = postMemberRepository.findByUsername(username).orElseThrow();

        assertThat(replica.getId()).isEqualTo(source.getId());
        assertThat(replica.getUsername()).isEqualTo(source.getUsername());
        assertThat(replica.getNickname()).isEqualTo(source.getNickname());
        assertThat(replica.getActivityScore()).isEqualTo(source.getActivityScore());
    }

    @Test
    void 활동점수_변경_시_복제본_점수가_동기화된다() {
        String username = "syncUser2";
        memberFacade.join(username, "pw1234!", "동기화유저2");
        PostMember author = postMemberRepository.findByUsername(username).orElseThrow();

        postFacade.write(author, "제목", "내용");

        Member source = memberRepository.findByUsername(username).orElseThrow();
        PostMember replica = postMemberRepository.findByUsername(username).orElseThrow();

        assertThat(source.getActivityScore()).isEqualTo(3);
        assertThat(replica.getActivityScore()).isEqualTo(3);
    }
}
