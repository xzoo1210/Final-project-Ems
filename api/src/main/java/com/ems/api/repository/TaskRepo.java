package com.ems.api.repository;

import com.ems.api.entity.Task;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    @Query(value = "select t from Task t where " +
            "((upper(coalesce(t.name,'')) like upper(concat('%', :searchKey,'%'))) " +
            "OR (upper(coalesce(t.code,'')) like upper(concat('%', :searchKey,'%'))) " +
            "OR :searchKey is null) " +
            "AND (t.eventId=:eventId or :eventId is null) " +
            "AND (t.assigneeId=:assigneeId or :assigneeId is null) " +
            "AND (t.status=:status or :status is null) " +
            "AND (t.assigneeId in (select t.memberId from TeamContact t where t.teamId=:teamId) or :teamId is null)",
            countQuery = "select count(t) from Task t where " +
                    "((upper(coalesce(t.name,'')) like upper(concat('%', :searchKey,'%'))) " +
                    "OR (upper(coalesce(t.code,'')) like upper(concat('%', :searchKey,'%'))) " +
                    "OR :searchKey is null) " +
                    "AND (t.eventId=:eventId or :eventId is null) " +
                    "AND (t.assigneeId=:assigneeId or :assigneeId is null) " +
                    "AND (t.status=:status or :status is null) " +
                    "AND (t.assigneeId in (select t.memberId from TeamContact t where t.teamId=:teamId) or :teamId is null)")
    Page<Task> search(@Param("searchKey") String searchKey,
                      @Param("eventId") Long eventId,
                      @Param("teamId") Long teamId,
                      @Param("assigneeId") Long assigneeId,
                      @Param("status") Constant.TypeConstant.TaskStatus status,
                      Pageable pageable);

    @Query(value = "select t from Task t where " +
            "((upper(coalesce(t.name,'')) like upper(concat('%', :searchKey,'%'))) " +
            "OR (upper(coalesce(t.code,'')) like upper(concat('%', :searchKey,'%'))) " +
            "OR :searchKey is null) " +
            "AND (t.status=:status or :status is null) " +
            "AND (t.assigneeId=:assigneeId) ",
            countQuery = "select count(t) from Task t where " +
                    "((upper(coalesce(t.name,'')) like upper(concat('%', :searchKey,'%'))) " +
                    "OR (upper(coalesce(t.code,'')) like upper(concat('%', :searchKey,'%'))) " +
                    "OR :searchKey is null) " +
                    "AND (t.status=:status or :status is null) " +
                    "AND (t.assigneeId=:assigneeId) ")
    Page<Task> findAllByAssigneeId(@Param("assigneeId") Long assigneeId,
                                   @Param("searchKey") String searchKey,
                                   @Param("status") Constant.TypeConstant.TaskStatus status,
                                   Pageable pageable);

    boolean existsByEventIdAndStatusIn(Long eventId, List<Constant.TypeConstant.TaskStatus> statuses);

    List<Task> findAllByEventIdAndAssigneeIdAndEndDateBefore(Long eventId, Long assigneeId, Date endDate);
}
