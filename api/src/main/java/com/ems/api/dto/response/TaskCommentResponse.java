package com.ems.api.dto.response;

import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCommentResponse {
    private ContactShortResponse creator;
    private Date createDate;
    private String content;
}
