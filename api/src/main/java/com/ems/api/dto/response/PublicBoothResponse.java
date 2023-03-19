package com.ems.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PublicBoothResponse {
    private Long id;
    private Double rentFee;
    private PublicBoothLocationResponse location;
    private String name;
    private String description;
    private Date startRentDate;
    private Date endRentDate;
}
