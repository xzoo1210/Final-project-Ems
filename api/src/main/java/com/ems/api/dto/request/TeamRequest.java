package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamRequest extends CreateContactRequest {
    private Long eventId;
    private String description;
}
