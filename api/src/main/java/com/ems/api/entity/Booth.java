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
@Table(name = "BOOTH")
public class Booth implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOTH_SEQ")
    @SequenceGenerator(sequenceName = "BOOTH_SEQ", allocationSize = 1, name = "BOOTH_SEQ")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private Contact creator;
    private Long creatorId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @ManyToOne
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;
    private Long eventId;

    private Double rentFee;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.BoothStatus status;

    @ManyToOne
    @JoinColumn(name = "locationId", insertable = false, updatable = false)
    private BoothLocation location;
    private Long locationId;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;


    @Temporal(TemporalType.TIMESTAMP)
    private Date startRentDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endRentDate;
}
