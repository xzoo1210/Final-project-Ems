package com.ems.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupTicketOrderDetailResponse {
    private String name;
    private Double price;
    private Integer quantity;
}
