package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {
    private String subject;
    private String content;
    private String overviewDescription;
    private String overviewImagePath;
}
