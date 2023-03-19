package com.ems.api.repository;


import com.ems.api.dto.SponsorExport;
import com.ems.api.entity.Sponsor;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsorRepo extends JpaRepository<Sponsor, Long> {
    @Query(value = "select s from Sponsor s " +
            "where s.eventId=:eventId",
    countQuery = "select count(s) from Sponsor s " +
            "where s.eventId=:eventId")
    Page<Sponsor> search(@Param("eventId") Long eventId, Pageable pageable);

    List<Sponsor> findAllByEventIdAndPublicSponsor(Long eventId, Constant.TypeConstant.PublicSponsor publicSponsor);

    List<Sponsor> findAllByEventId(Long eventId);

@Query(value = "select s.name,    " +
        " s.email,    " +
        " s.phone,    " +
        " s.link,    " +
        " s.sponsor_item item,    " +
        "s.more_information  information    " +
        " from ems.sponsor s where s.event_id=:eventId" +
        " order by name",nativeQuery = true)
    List<SponsorExport> findAllByEventIdForExport(@Param("eventId")Long eventId);
}
