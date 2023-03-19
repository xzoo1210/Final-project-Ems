package com.ems.api.repository;

import com.ems.api.entity.ActivityLogDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogDetailRepo extends JpaRepository<ActivityLogDetail, Long> {
}
