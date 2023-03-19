package com.ems.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BoothRequest {
    private Long eventId;
    private Double rentFee;
    private Long locationId;
    private String name;
    private String description;
    private Date startRentDate;
    private Date endRentDate;
}
