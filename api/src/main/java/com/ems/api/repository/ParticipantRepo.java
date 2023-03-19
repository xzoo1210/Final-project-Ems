package com.ems.api.repository;


import com.ems.api.entity.Participant;
import com.ems.api.dto.ParticipantExport;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepo extends JpaRepository<Participant, Long> {
    @Query(value = "select p from Participant p where " +
            "p.eventId = :eventId " +
            "and (p.ticketId=:ticketId or :ticketId is null)" +
            "and (" +
            "(upper(coalesce(p.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(p.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(p.name,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL" +
            ") ",countQuery = "select count(p) from Participant p where " +
            "p.eventId = :eventId " +
            "and (p.ticketId=:ticketId or :ticketId is null)" +
            "and (" +
            "(upper(coalesce(p.email,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(p.phone,'')) like upper(concat('%', :keyword,'%')))" +
            " OR (upper(coalesce(p.name,'')) like upper(concat('%', :keyword,'%')))" +
            " OR :keyword IS NULL" +
            ") ")
    Page<Participant> search(@Param("eventId")Long eventId,
                             @Param("ticketId")Long ticketId,
                             @Param("keyword")String keySearch, Pageable pageable);

    List<Participant> findAllByEventId(Long eventId);


    @Query(value="select t.name TicketName, " +
            "i.first_name InviterName, " +
            "p.name ParticipantName, " +
            "p.email, " +
            "p.phone, " +
            "p.added_date AddedDate, " +
            "p.confirmed_date ConfirmedDate, " +
            "CASE " +
            "    WHEN p.status = 'WAITING' THEN 'Chưa tham gia' " +
            "    WHEN p.status = 'ACCEPT' THEN 'Đã tham gia' " +
            "END status, " +
            "v.first_name VerifierName " +
            "from ems.participant p " +
            "left join ems.contact i on p.inviter_id=i.id " +
            "join ems.ticket t on p.ticket_id=t.id " +
            "left join ems.contact v on p.verifier_id=v.id " +
            "where p.event_id=:eventId " +
            "order by p.id desc",nativeQuery = true)
    List<ParticipantExport> findAllByEventIdForExport(@Param("eventId")Long eventId);
    List<Participant> findAllByEventIdAndStatus(Long eventId, Constant.TypeConstant.ParticipantStatus status);
}
