package com.ems.api.service.impl;

import com.ems.api.entity.ActivityLog;
import com.ems.api.dto.response.ActivityLogResponse;
import com.ems.api.repository.ActivityLogRepo;
import com.ems.api.service.ActivityLogService;
import com.ems.api.util.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {
    @Autowired
    private ActivityLogRepo activityLogRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ActivityLog create(ActivityLog activityLog) {
        return activityLogRepo.save(activityLog);
    }

    @Override
    public List<ActivityLogResponse> findByType(Constant.TypeConstant.ActivityType activityType, Long featureId) {
        List<ActivityLogResponse> alr =
                activityLogRepo.findAllByActivityTypeAndFeatureIdOrderByIdDesc(activityType, featureId)
                        .stream().map(l -> {
                    ActivityLogResponse response = modelMapper.map(l, ActivityLogResponse.class);
                    response.setActivityLogDetails(l.getActivityLogDetails());
                    return response;
                }).collect(Collectors.toList());


        return alr;
    }
}
