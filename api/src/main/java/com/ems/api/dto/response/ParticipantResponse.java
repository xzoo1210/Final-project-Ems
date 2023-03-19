package com.ems.api.dto.response;

import com.ems.api.util.Constant;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponse {
    private Long id;
    private EventResponse event;
    private ContactShortResponse inviter;
    private PublicTicketResponse ticket;
    private Constant.TypeConstant.ParticipantStatus status;

    private String name;
    private String email;
    private String phone;

    private Date addedDate;
    private Date confirmedDate;
    private ContactShortResponse verifier;

}
