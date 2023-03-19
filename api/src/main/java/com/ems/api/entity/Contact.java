package com.ems.api.entity;

import com.ems.api.dto.response.EventResponse;
import com.ems.api.dto.response.EventShortResponse;
import com.ems.api.dto.response.TeamShortResponse;
import com.ems.api.util.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "CONTACT")
public class Contact implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTACT_SEQ")
    @SequenceGenerator(sequenceName = "CONTACT_SEQ", allocationSize = 1, name = "CONTACT_SEQ")
    private Long id;

    @OneToOne
    @JoinColumn(name = "emailAccount", insertable = false, updatable = false)
    private Account account;

    private String emailAccount;
    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.ContactType contactType;
    private String email;
    private String phone;
    private String mobile;
    private String websiteLink;

    private String imagePath;

    private String firstName;
    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Event eventTeam = null;
    private Long eventId;

    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate = new Date();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<EventSponsor> eventSponsors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact ownerContact;
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressId", insertable = false, updatable = false)
    private Address address;
    private Long addressId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false, nullable = true)
    @JsonIgnore
    private Collection<Event> createdEvent;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false, nullable = true)
    @JsonIgnore
    private Collection<TeamContact> joinedTeams;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", insertable = false, updatable = false, nullable = true)
    @JsonIgnore
    private Collection<EventContact> joinedEvents;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", insertable = false, updatable = false, nullable = true)
    @JsonIgnore
    private Collection<TeamContact> teamMembers;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false, nullable = true)
    @JsonIgnore
    private Collection<UserContact> addedUserContacts;

    @Transient
    private Collection<TeamShortResponse> teamsInCommon;

    @Transient
    private Collection<EventShortResponse> eventsInCommon;

    @Transient
    private Collection<Contact> addedContactUsers;

    @Transient
    private Boolean isAdded;

    @Transient
    private Integer noLateTask=0;
    @Transient
    private Collection<TeamShortResponse> teamsInEvent;
}
