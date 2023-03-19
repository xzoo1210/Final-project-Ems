package com.ems.api.dto.request;

import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
    private String name;
    private Long eventId;
    private Date startDate;
    private Date endDate;
    private Long parenTaskId;
    private Long assigneeId;
    private String description;
}
