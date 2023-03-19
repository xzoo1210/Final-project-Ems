package com.ems.api.entity;

import com.ems.api.util.Constant;
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
@Table(name = "SPONSOR")
public class Sponsor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SPONSOR_SEQ")
    @SequenceGenerator(sequenceName = "SPONSOR_SEQ", allocationSize = 1, name = "SPONSOR_SEQ")
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String sponsorItem;
    @Column(columnDefinition = "TEXT")
    private String moreInformation;
    private String link;

    private String logoPath;

    private Constant.TypeConstant.PublicSponsor publicSponsor;

    private Long eventId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private Contact creator;
    private Long creatorId;
}
