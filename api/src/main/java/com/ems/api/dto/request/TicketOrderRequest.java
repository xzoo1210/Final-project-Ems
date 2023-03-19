package com.ems.api.dto.request;

import lombok.Data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketOrderRequest {
    private List<TicketOrderDetailRequest> orderDetailRequests;
    private AddressRequest addressRequest;
    private Double amount;
    private String name;
    private String email;
    private String phone;
    private Long eventId;
}
