package com.ems.api.repository;

import com.ems.api.entity.BoothLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoothLocationRepo extends JpaRepository<BoothLocation, Long> {
    Page<BoothLocation> findAllByEventId(Long eventId, Pageable pageable);
}
