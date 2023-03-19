package com.ems.api.dto.request;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantRequest {
    private String name;
    private String email;
    private String phone;
    private Long ticketId;
    private Long eventId;
}
