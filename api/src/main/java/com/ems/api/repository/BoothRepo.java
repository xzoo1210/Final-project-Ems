package com.ems.api.repository;

import com.ems.api.dto.BoothExport;
import com.ems.api.entity.Booth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoothRepo extends JpaRepository<Booth, Long> {
    Page<Booth> findAllByEventId(Long eventId, Pageable pageable);

    boolean existsByLocationId(Long locationId);

    @Query(value = "select l.name LocationName,  " +
            " l.description LocationDescription,  " +
            " b.name BoothName,  " +
            " b.description BoothDescription,  " +
            " b.rent_fee RentFee,  " +
            " b.start_rent_date StartRentDate,  " +
            " b.end_rent_date EndRentDate,  " +
            " CASE " +
            "    WHEN b.status = 'ACTIVE' THEN 'Hoạt động' " +
            "    WHEN b.status = 'NOT_ACTIVE' THEN 'Không hoạt động' " +
            " END status,  " +
            " (select count(1) from ems.booth_order o where b.id=o.booth_id and status='WAITING') Waiting,  " +
            " (select count(1) from ems.booth_order o where b.id=o.booth_id and status='ACCEPT') Accept,    " +
            " (select count(1) from ems.booth_order o where b.id=o.booth_id and status='REJECT') Reject,    " +
            " (select count(1) from ems.booth_order o where b.id=o.booth_id)  TotalOrder  " +
            "from ems.booth b  " +
            "join ems.booth_location l on b.location_id=l.id  " +
            "where b.event_id = :eventId  " +
            "order by l.name",nativeQuery = true)
    List<BoothExport> findAllByEventIdForExport(@Param("eventId") Long eventId);
}
