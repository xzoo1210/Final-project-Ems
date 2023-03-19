package com.ems.api.dto.response;

import com.ems.api.entity.ActivityLogDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityLogResponse {
    private ContactShortResponse actor;
    private Date createDate;
    private String note;
    private List<ActivityLogDetail> activityLogDetails;
}
