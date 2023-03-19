package com.ems.api.service.impl;

import com.ems.api.entity.ActivityLogDetail;
import com.ems.api.repository.ActivityLogDetailRepo;
import com.ems.api.service.ActivityLogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActivityLogDetailServiceImpl implements ActivityLogDetailService {
    @Autowired
    private ActivityLogDetailRepo activityLogDetailRepo;

    @Override
    public List<ActivityLogDetail> saveDetails(List<ActivityLogDetail> activityLogDetails) {
        return activityLogDetailRepo.saveAll(activityLogDetails);
    }
}
