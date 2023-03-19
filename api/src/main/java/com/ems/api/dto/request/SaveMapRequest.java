package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveMapRequest {
    private Long eventId;
    private String mapPath;
}
