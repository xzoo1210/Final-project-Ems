package com.ems.api.dto.response;

import com.ems.api.entity.TicketOrder;
import com.ems.api.entity.TicketOrderDetail;
import lombok.Data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketOrderResponse {
    private TicketOrder ticketOrder;
    private List<TicketOrderDetail> ticketOrderDetails;
}
