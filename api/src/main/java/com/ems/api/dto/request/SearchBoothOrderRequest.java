package com.ems.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBoothOrderRequest extends PagingRequest {
    private Long eventId;
    private Long boothId;
}
