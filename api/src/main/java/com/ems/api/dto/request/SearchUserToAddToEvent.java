package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserToAddToEvent {
    private String keyword;
    private Long eventId;
}
