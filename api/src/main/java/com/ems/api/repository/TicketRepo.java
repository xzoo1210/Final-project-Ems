package com.ems.api.repository;

import com.ems.api.dto.TicketExport;
import com.ems.api.entity.Ticket;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {
    Page<Ticket> findAllByEventId(Long eventId, Pageable pageable);

    List<Ticket> findAllByEventId(Long eventId);

    @Query(value = "select t.name," +
            "c.first_name creatorName," +
            "t.create_date createDate," +
            "t.limited," +
            "t.price," +
            "t.sold_quantity sold," +
            "t.limited - t.remaining_quantity - t.sold_quantity invited," +
            "t.remaining_quantity remain," +
            "CASE" +
            "    WHEN t.status = 'ACTIVE' THEN 'Hoạt động'" +
            "    WHEN t.status = 'NOT_ACTIVE' THEN 'Không hoạt động'" +
            "END status" +
            " from ems.ticket t join ems.contact c on t.creator_id=c.id " +
            " where t.event_id=:eventId" +
            " order by t.create_date", nativeQuery = true)
    List<TicketExport> findAllByEventIdForExport(@Param("eventId") Long eventId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "Select t from Ticket t where t.id=:ticketId")
    Ticket findByIdForUpdate(@Param("ticketId") Long ticketId);

    List<Ticket> findAllByEventIdAndStatusOrderByPriceDesc(Long eventId, Constant.TypeConstant.TicketStatus status);
}
