package com.ems.api.entity;

import com.ems.api.util.Constant;
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
@Table(name = "PARTICIPANT")
public class Participant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARTICIPANT_SEQ")
    @SequenceGenerator(sequenceName = "PARTICIPANT_SEQ", allocationSize = 1, name = "PARTICIPANT_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", updatable = false, insertable = false)
    private Event event;
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviterId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact inviter;
    private Long inviterId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId", insertable = false, updatable = false)
    @JsonIgnore
    private Ticket ticket;
    private Long ticketId;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.ParticipantStatus status;

    private String name;
    private String email;
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date addedDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date confirmedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verifierId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact verifier;
    private Long verifierId;
}
