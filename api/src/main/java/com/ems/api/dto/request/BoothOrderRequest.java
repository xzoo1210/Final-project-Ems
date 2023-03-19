package com.ems.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BoothOrderRequest {
    private String name;
    private String email;
    private String phone;
    private Long boothId;
    private Date startRentDate;
    private Date endRentDate;
}
