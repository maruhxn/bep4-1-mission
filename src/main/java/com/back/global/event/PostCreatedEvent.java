package com.back.global.event;

import com.back.global.dto.PostDto;

public record PostCreatedEvent(PostDto post) {
}