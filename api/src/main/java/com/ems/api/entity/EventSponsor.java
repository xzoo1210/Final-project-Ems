package com.ems.api.entity;

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
@Table(name = "EVENT_SPONSOR")
public class EventSponsor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EVENT_SPONSOR_SEQ")
    @SequenceGenerator(sequenceName = "EVENT_SPONSOR_SEQ", allocationSize = 1, name = "EVENT_SPONSOR_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;
    private Long eventId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sponsorId", insertable = false, updatable = false)
    private Sponsor sponsor;
    private Long sponsorId;

    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate = new Date();

    @Override
    public EventSponsor clone() throws CloneNotSupportedException {
        return (EventSponsor) super.clone();
    }
}
