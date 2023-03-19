package com.ems.api.dto.response;

import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicEventPostShortResponse {
    private Long id;
    private String subject;
    private Date createDate;
    private String overviewDescription;
    private String overviewImagePath;
    private Integer views;
}
