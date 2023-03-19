package com.ems.api.entity;

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
@Table(name = "AGENDA")
public class Agenda implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AGENDA_SEQ")
    @SequenceGenerator(sequenceName = "AGENDA_SEQ", allocationSize = 1, name = "AGENDA_SEQ")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Event event;
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact creator;
    private Long creatorId;
}
