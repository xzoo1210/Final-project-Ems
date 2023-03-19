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
@Table(name = "POST")
public class Post implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ")
    @SequenceGenerator(sequenceName = "POST_SEQ", allocationSize = 1, name = "POST_SEQ")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creatorId", updatable = false, insertable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Contact creator;

    private Long creatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact team;

    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    @JsonIgnore
    private Event event;

    private Long eventId;

    @Column(columnDefinition = "TEXT")
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.PostType postType;

    @Column(columnDefinition = "TEXT")
    private String overviewDescription;

    @Column(columnDefinition = "TEXT")
    private String overviewImagePath;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.PostStatus status;

    private Integer views = 0;

    private String likes = "";

    @Transient
    private Integer noComment = 0;
    @Transient
    private Integer noLike = 0;
    @Transient
    private Boolean isLiked = false;

}
