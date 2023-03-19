package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCommentRequest {
    private Long taskId;
    private String content;
}
