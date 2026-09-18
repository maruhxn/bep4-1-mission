package com.back.integration;

import com.back.bounded_context.member.app.MemberFacade;
import com.back.bounded_context.member.domain.Member;
import com.back.bounded_context.member.out.MemberRepository;
import com.back.bounded_context.post.app.PostFacade;
import com.back.bounded_context.post.domain.Post;
import com.back.bounded_context.post.domain.PostMember;
import com.back.bounded_context.post.out.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 글/댓글 작성 시 활동 점수 증가와, 작성 트랜잭션 롤백 시 점수가 증가하지 않음을 검증한다.
 * MemberApiClient가 localhost:8080으로 자기 자신을 호출하므로 DEFINED_PORT로 실서버를 띄운다.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:integration_test;MODE=MySQL;DB_CLOSE_DELAY=-1"
)
class PostActivityScoreIntegrationTest {

    @Autowired
    private MemberFacade memberFacade;
    @Autowired
    private PostFacade postFacade;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;

    private PostMember joinAndGetReplica(String username) {
        memberFacade.join(username, "pw1234!", username + "-nick");
        return postFacade.findMemberByUsername(username).orElseThrow();
    }

    @Test
    void 글_작성_시_활동점수가_3점_증가한다() {
        String username = "scoreUser1";
        PostMember author = joinAndGetReplica(username);

        postFacade.write(author, "제목1", "내용1");

        Member member = memberRepository.findByUsername(username).orElseThrow();
        assertThat(member.getActivityScore()).isEqualTo(3);
    }

    @Test
    void 댓글_작성_시_활동점수가_1점_증가한다() {
        PostMember postAuthor = joinAndGetReplica("scoreUser2");
        Post post = postFacade.write(postAuthor, "제목2", "내용2").data();

        String commenterUsername = "scoreUser3";
        PostMember commenter = joinAndGetReplica(commenterUsername);

        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            Post managedPost = postRepository.findById(post.getId()).orElseThrow();
            managedPost.addComment(commenter, "댓글 내용");
        });

        Member commenterMember = memberRepository.findByUsername(commenterUsername).orElseThrow();
        assertThat(commenterMember.getActivityScore()).isEqualTo(1);
    }

    @Test
    void 게시글_작성이_롤백되면_활동점수가_증가하지_않는다() {
        String username = "scoreUser4";
        PostMember author = joinAndGetReplica(username);

        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            postFacade.write(author, "롤백제목", "롤백내용");
            status.setRollbackOnly();
        });

        Member member = memberRepository.findByUsername(username).orElseThrow();
        assertThat(member.getActivityScore()).isEqualTo(0);
        assertThat(postRepository.findAll()).noneMatch(p -> p.getTitle().equals("롤백제목"));
    }
}
