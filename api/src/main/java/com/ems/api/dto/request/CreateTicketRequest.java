package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest extends TicketRequest {

    private Long eventId;
    private Double price;

}
