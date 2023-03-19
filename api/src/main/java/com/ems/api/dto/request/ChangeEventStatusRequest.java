package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeEventStatusRequest {
    private Long eventId;
    private Constant.TypeConstant.EventStatus status;
    private boolean checkTaskDone = false;
    private String note;
    private String contentMail;
}
