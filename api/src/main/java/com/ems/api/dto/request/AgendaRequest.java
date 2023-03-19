package com.ems.api.dto.request;

import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgendaRequest {
    private Long id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Long eventId;
}
