package com.back.global.event;

import com.back.global.dto.PostCommentDto;

public record PostCommentCreatedEvent(PostCommentDto postComment) {
}