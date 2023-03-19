package com.ems.api.dto.response;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamShortResponse {
    private Long id;
    private String name;
    private String imagePath;
    private String description;
    private ContactShortResponse creator;
    private int noMembers;
}
