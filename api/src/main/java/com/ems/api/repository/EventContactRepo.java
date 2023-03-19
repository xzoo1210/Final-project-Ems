package com.ems.api.repository;

import com.ems.api.dto.EventMemberExport;
import com.ems.api.entity.EventContact;
import com.ems.api.util.Constant.TypeConstant.EventMemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventContactRepo extends JpaRepository<EventContact, Long> {
    Optional<EventContact> findByEventIdAndMemberId(Long eventId, Long memberId);

    List<EventContact> findAllByEventIdAndStatus(Long eventId, EventMemberStatus status);

    List<EventContact> findAllByMemberIdAndStatus(Long memberId, EventMemberStatus status);

    @Query(value = "select     " +
            " CASE    " +
            "    WHEN  m.event_role = 'HEADER' THEN 'Trưởng ban tổ chức'    " +
            "    WHEN  m.event_role = 'SUB_HEADER' THEN 'Quản trị viên'    " +
            "    WHEN   m.event_role = 'MEMBER' THEN 'Thành viên'    " +
            "END role,    " +
            "mc.first_name name,    " +
            "(select count(1) from ems.team_contact tc join ems.contact t on tc.team_id=t.id where t.event_id=m.event_id) noTeam,    " +
            "m.create_date createDate,    " +
            "c.first_name creatorName,    " +
            "(select count(1) from ems.task t where t.event_id=m.event_id and t.assignee_id=m.member_id and t.status='OPEN') taskOpen,    " +
            "(select count(1) from ems.task t where t.event_id=m.event_id and t.assignee_id=m.member_id and t.status='INPROGRESS') taskInprogress,    " +
            "(select count(1) from ems.task t where t.event_id=m.event_id and t.assignee_id=m.member_id and t.status='DONE') taskDone,    " +
            "(select count(1) from ems.task t where t.event_id=m.event_id and t.assignee_id=m.member_id and t.status='CANCEL') taskCancel,    " +
            "(select count(1) from ems.task t where t.event_id=m.event_id and t.assignee_id=m.member_id and t.end_date<=CURDATE()  and t.status not in ('DONE','CANCEL')) taskLate,    " +
            "(select count(1) from ems.task t where t.event_id=m.event_id and t.assignee_id=m.member_id) task    " +
            " from ems.event_contact m    " +
            " join ems.contact mc on m.member_id=mc.id    " +
            " join ems.contact c on m.creator_id=c.id    " +
            " where m.event_id=:eventId    " +
            " order by m.create_date, role desc",nativeQuery = true)
    List<EventMemberExport> findAllByEventIdForExport(@Param("eventId") Long eventId);
}
