package com.back.bounded_context.post.domain;

import static jakarta.persistence.FetchType.LAZY;

import com.back.global.jpa.BaseIdAndTime;
import com.back.shared.post.dto.PostDto;
import com.back.shared.post.event.PostCommentCreatedEvent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "POST_POST")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseIdAndTime {
    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private final List<PostComment> comments = new ArrayList<>();
    @ManyToOne(fetch = LAZY)
    private PostMember author;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;


    public Post(PostMember author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public PostComment addComment(PostMember author, String content) {
        PostComment postComment = new PostComment(this, author, content);

        comments.add(postComment);

        // 댓글 작성 시, 활동 점수 +1
        publishEvent(new PostCommentCreatedEvent(postComment.toDto()));

        return postComment;
    }

    public boolean hasComments() {
        return !comments.isEmpty();
    }

    public PostDto toDto() {
        return new PostDto(
                getId(),
                getCreateDate(),
                getModifyDate(),
                author.getId(),
                author.getNickname(),
                title,
                content
        );
    }

}