package com.ems.api.dto.response;

import com.ems.api.util.Constant;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventContactResponse {
    private Long id;
    private ContactShortResponse member;
    private Constant.TypeConstant.EventMemberStatus status;
    private Constant.TypeConstant.EventRole eventRole;
    private Constant.TypeConstant.EventMemberAccess organizationAccess;

    private Constant.TypeConstant.EventMemberAccess agendaAccess;

    private Constant.TypeConstant.EventMemberAccess ticketAccess;

    private Constant.TypeConstant.EventMemberAccess boothAccess;

    private Constant.TypeConstant.EventMemberAccess sponsorAccess;

    private Constant.TypeConstant.EventMemberAccess postAccess;

    private Constant.TypeConstant.EventMemberAccess participantAccess;

    private Date createDate;
    private ContactShortResponse creator;
    private EventResponse event;
}
