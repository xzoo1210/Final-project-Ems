package com.ems.api.dto.request;

import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequest {
    private Date saleBeginDate;
    private Date saleEndDate;
    private String description;
    private String name;
    private Integer limited;
}
