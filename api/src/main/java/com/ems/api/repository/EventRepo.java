package com.ems.api.repository;

import com.ems.api.entity.Event;
import com.ems.api.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {
    @Query(value = "select distinct e from EventContact ec join ec.event e where " +
            "((upper(coalesce(e.name,'')) like upper(concat('%', :keyword,'%'))) " +
            "OR (upper(coalesce(e.code,'')) like upper(concat('%', :keyword,'%'))) " +
            "OR :keyword IS NULL ) " +
            "AND ec.member.emailAccount = :userEmail " +
            "AND ec.status= :memberStatus "+
            "AND (e.status= :status or :status is null) ",
            countQuery = "select count(distinct e) from EventContact ec join ec.event e where " +
                    "((upper(coalesce(e.name,'')) like upper(concat('%', :keyword,'%'))) " +
                    "OR (upper(coalesce(e.code,'')) like upper(concat('%', :keyword,'%'))) " +
                    "OR :keyword IS NULL ) " +
                    "AND ec.member.emailAccount = :userEmail " +
                    "AND ec.status= :memberStatus "+
                    "AND (e.status= :status or :status is null)")
    Page<Event> search(@Param("keyword") String keyword,
                       @Param("userEmail") String userEmail,
                       @Param("memberStatus") Constant.TypeConstant.EventMemberStatus memberStatus,
                       @Param("status") Constant.TypeConstant.EventStatus status,
                       Pageable pageable);


    @Query(value = "select e from Event e where upper(e.code)=upper(:code) ")
    Event findByCode(@Param("code") String code);

}
