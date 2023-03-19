package com.ems.api.entity;

import com.ems.api.util.Constant;
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
@Table(name = "TICKET")
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TICKET_SEQ")
    @SequenceGenerator(sequenceName = "TICKET_SEQ", allocationSize = 1, name = "TICKET_SEQ")
    private Long id;

    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private Contact creator;
    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;
    private Long eventId;

    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleBeginDate;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date saleEndDate;

    private Integer limited;

    private Integer remainingQuantity = limited;
    private Integer soldQuantity = 0;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;


    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate = new Date();

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.TicketStatus status;
}
