package com.ems.api.service;

import com.ems.api.entity.ActivityLog;
import com.ems.api.dto.response.ActivityLogResponse;
import com.ems.api.util.Constant;

import java.util.List;

public interface ActivityLogService {
    ActivityLog create(ActivityLog activityLog);

    List<ActivityLogResponse> findByType(Constant.TypeConstant.ActivityType activityType, Long featureId);
}
