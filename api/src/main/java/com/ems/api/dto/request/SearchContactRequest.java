package com.ems.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchContactRequest extends PagingRequest {
    private String keyword;
    private Long eventId;
    private Long teamId;
    private boolean findAddedUser=false;
}
