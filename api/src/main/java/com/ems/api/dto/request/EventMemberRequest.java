package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMemberRequest {
    private Long eventId;
    private Long memberId;
    private Constant.TypeConstant.EventMemberAccess organizationAccess;
    private Constant.TypeConstant.EventMemberAccess agendaAccess;
    private Constant.TypeConstant.EventMemberAccess ticketAccess;
    private Constant.TypeConstant.EventMemberAccess boothAccess;
    private Constant.TypeConstant.EventMemberAccess sponsorAccess;
    private Constant.TypeConstant.EventMemberAccess postAccess;
    private Constant.TypeConstant.EventMemberAccess participantAccess;
    private Boolean isSubHeader;
}
