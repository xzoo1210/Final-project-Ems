package com.ems.api.dto.response;

import java.util.Date;

import com.ems.api.util.Constant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskShortResponse {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private Integer progress;
    private String code;
    private Date createDate;
    private EventResponse event;
    private ContactShortResponse assignee;
    private ContactShortResponse assigner;
    private Constant.TypeConstant.TaskStatus status;
}
