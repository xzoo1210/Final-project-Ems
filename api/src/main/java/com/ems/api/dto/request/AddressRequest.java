package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private Long id;
    private String street1;
    private String street2;
    private String province;
    private String district;
    private String commune;
}
