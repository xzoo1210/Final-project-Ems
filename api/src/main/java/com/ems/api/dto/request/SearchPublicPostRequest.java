package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPublicPostRequest extends PagingRequest {
    private String searchKey;
    private Long eventId;
}
