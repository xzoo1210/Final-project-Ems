package com.ems.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    private Long id;
    private String code;
    private String name;
    private Date beginDate;
    private Date endDate;
    private String tags;
    private Boolean autoConfirm;
    private String description;
    private String note;
    private String logoPath;
}
