package com.ems.api.dto.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgendaResponse {
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private EventResponse event;
    private ContactShortResponse creator;
    private Date createDate;
    private Long id;
}
