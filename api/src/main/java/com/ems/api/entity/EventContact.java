package com.ems.api.entity;

import com.ems.api.util.Constant.TypeConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "EVENT_CONTACT")
public class EventContact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_CONTACT_SEQ")
    @SequenceGenerator(sequenceName = "EVENT_CONTACT_SEQ", allocationSize = 1, name = "EVENT_CONTACT_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact member;
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;
    private Long eventId;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberStatus status;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventRole eventRole;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberAccess organizationAccess;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberAccess agendaAccess;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberAccess ticketAccess;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberAccess boothAccess;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberAccess sponsorAccess;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberAccess postAccess;
    @Enumerated(EnumType.STRING)
    private TypeConstant.EventMemberAccess participantAccess;

    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private Contact creator;
    private Long creatorId;
}
