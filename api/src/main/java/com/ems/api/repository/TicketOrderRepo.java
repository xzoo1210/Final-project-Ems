package com.ems.api.repository;


import com.ems.api.dto.TicketOrderExport;
import com.ems.api.entity.TicketOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketOrderRepo extends JpaRepository<TicketOrder, Long> {
    @Query(value = "select to from TicketOrder to " +
            "WHERE to.eventId=:eventId " +
            "AND (to.code=:keySearch" +
            " or upper(coalesce(to.email,'')) like upper(concat('%', :keySearch,'%'))" +
            " or :keySearch is null)",
countQuery = "select count(to) from TicketOrder to " +
        "WHERE to.eventId=:eventId " +
        "AND (to.code=:keySearch" +
        " or upper(coalesce(to.email,'')) like upper(concat('%', :keySearch,'%'))" +
        " or :keySearch is null)"   )
    Page<TicketOrder> searchTicketOrder(@Param("eventId") Long eventId,
                                        @Param("keySearch") String keySearch,
                                        Pageable pageable);

    @Query(value = "select name, " +
            "phone, " +
            "email, " +
            "count(id) totalOrder, " +
            "sum(quantity) TotalQuantity, " +
            "sum(amount) TotalAmount " +
            "from ems.ticket_order where event_id=:eventId  " +
            "group by name,phone,email " +
            "order by sum(amount) desc", nativeQuery = true)
    List<TicketOrderExport> findAllByEventIdForExport(@Param("eventId") Long eventId);
}
