package com.ems.api.dto.response;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicSponsorResponse {
    private String name;
    private String phone;
    private String email;
    private String link;
    private String logoPath;
}
