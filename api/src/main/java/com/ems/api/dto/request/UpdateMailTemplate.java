package com.ems.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMailTemplate {
    private Long eventId;
    private String contentMail;
}
