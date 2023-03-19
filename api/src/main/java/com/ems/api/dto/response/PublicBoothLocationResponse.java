package com.ems.api.dto.response;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicBoothLocationResponse {
    private Long id;
    private String name;
    private String description;
}
