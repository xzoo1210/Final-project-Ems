package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketOrderDetailRequest {
    private String name;
    private String email;
    private String phone;
    private Long ticketId;
}
