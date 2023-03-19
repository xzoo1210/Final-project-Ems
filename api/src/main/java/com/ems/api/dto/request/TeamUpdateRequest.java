package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamUpdateRequest extends TeamRequest {
    private Long id;
}
