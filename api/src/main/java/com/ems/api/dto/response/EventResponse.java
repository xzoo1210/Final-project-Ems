package com.ems.api.dto.response;

import com.ems.api.util.Constant;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventResponse {
    private Long id;
    private String name;
    private String code;
    private String logoPath;
    private Integer noMembers;
    private Integer noParticipants;
    private Integer noBooths;
    private Integer noTickets;
    private Integer noSponsors;
    private ContactShortResponse creator;
    private Date beginDate;
    private Date endDate;
    private String description;
    private String contentTicketMail;
    private String contentBuyTicketMail;
    private String contentInviteMail;
    private String note;
    private Boolean autoConfirm;
    private Constant.TypeConstant.EventStatus status;
}
