package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTicketOrderRequest extends PagingRequest {
    private Long eventId;
    private String keySearch;
}
