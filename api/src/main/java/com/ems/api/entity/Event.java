package com.ems.api.entity;

import com.ems.api.util.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "EVENT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_SEQ")
    @SequenceGenerator(sequenceName = "EVENT_SEQ", allocationSize = 1, name = "EVENT_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private Contact creator;
    private Long creatorId;

    @Transient
    private Contact incharge = this.getCreator();

    private String name;
    private String code;
    @Column(columnDefinition = "TEXT")
    private String note;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Boolean autoConfirm = false;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date beginDate;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualBeginDate;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualEndDate;

    private String logoPath;
    private String mapPath;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<EventSponsor> eventSponsors;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<Booth> booths;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<Ticket> tickets;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<Post> posts;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<Contact> teams;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<Sponsor> sponsors;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<Participant> participants;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    //members
    private Collection<EventContact> eventContacts;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.SaleTicket saleTicket = Constant.TypeConstant.SaleTicket.DISABLE;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.RentBooth rentBooth = Constant.TypeConstant.RentBooth.DISABLE;

    @Column(columnDefinition = "TEXT")
    private String contentTicketMail;
    @Column(columnDefinition = "TEXT")
    private String contentBuyTicketMail;
    @Column(columnDefinition = "TEXT")
    private String contentInviteMail;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.EventStatus status = Constant.TypeConstant.EventStatus.INPROGRESS;
}
