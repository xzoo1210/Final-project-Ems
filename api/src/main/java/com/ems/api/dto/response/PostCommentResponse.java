package com.ems.api.dto.response;

import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentResponse {
    private Long id;
    private ContactShortResponse creator;
    private String content;
    private Date createDate;
}
