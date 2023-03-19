package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentRequest {
    private Long postId;
    private String content;
    private Long id;
}
