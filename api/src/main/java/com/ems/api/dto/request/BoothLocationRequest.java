package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothLocationRequest {
    private Long eventId;
    private String name;
    private String description;
}
