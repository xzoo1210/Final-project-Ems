package com.ems.api.service;

import com.ems.api.entity.ActivityLogDetail;

import java.util.List;

public interface ActivityLogDetailService {
    List<ActivityLogDetail> saveDetails(List<ActivityLogDetail> activityLogDetails);
}
