package com.back.bounded_context.post.domain;

import com.back.shared.post.dto.PostCommentDto;
import com.back.shared.post.event.PostCommentCreatedEvent;
import com.back.global.jpa.BaseIdAndTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Table(name = "POST_POST")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseIdAndTime {
    @ManyToOne(fetch = LAZY)
    private PostMember author;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();


    public Post(PostMember author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public PostComment addComment(PostMember author, String content) {
        PostComment postComment = new PostComment(this, author, content);

        comments.add(postComment);

        // 댓글 작성 시, 활동 점수 +1
        publishEvent(new PostCommentCreatedEvent(new PostCommentDto(postComment)));

        return postComment;
    }

    public boolean hasComments() {
        return !comments.isEmpty();
    }
}