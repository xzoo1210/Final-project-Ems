package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTaskRequest extends PagingRequest {
    private String searchKey;
    private Long eventId;
    private Long teamId;
    private Long assigneeId;
    private Long currentTaskId;
    private Constant.TypeConstant.TaskStatus status;
}
