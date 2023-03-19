package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {
    private Long id;
    private String name;
    private Date startDate;
    private Date endDate;
    private Long parentTaskId;
    private Long assigneeId;
    private String description;
    private String note;
    private Integer progress;
    private Constant.TypeConstant.TaskStatus status;
}
