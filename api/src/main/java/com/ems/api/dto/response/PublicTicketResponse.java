package com.ems.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PublicTicketResponse {
    private Long id;
    private Date saleBeginDate;
    private Date saleEndDate;
    private Integer remainingQuantity;
    private String name;
    private String description;
    private Double price;
}
