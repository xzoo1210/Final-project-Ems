package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothLocationUpdateRequest extends BoothLocationRequest {
    private Long id;
}
