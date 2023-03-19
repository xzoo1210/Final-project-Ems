package com.ems.api.repository;

import com.ems.api.entity.ActivityLog;
import com.ems.api.util.Constant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepo extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findAllByActivityTypeAndFeatureIdOrderByIdDesc(Constant.TypeConstant.ActivityType activityType, Long featureId);
}
