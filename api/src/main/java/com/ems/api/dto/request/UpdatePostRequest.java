package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostRequest {
    private Long id;
    private Long eventId;
    private String content;
    private String subject;
    private String overviewDescription;
    private String overviewImagePath;
    private Constant.TypeConstant.PostStatus status;
}
