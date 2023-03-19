package com.ems.api.repository;


import com.ems.api.entity.TicketOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketOrderDetailRepo extends JpaRepository<TicketOrderDetail, Long> {
    List<TicketOrderDetail> findAllByTicketOrderId(Long ticketOrderId);
}
