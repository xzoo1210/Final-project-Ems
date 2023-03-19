package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContactRequest {
    private Long id;
    private String email;
    private String phone;
    private String mobile;
    private String websiteLink;

    private String imagePath;

    private String firstName;
}
