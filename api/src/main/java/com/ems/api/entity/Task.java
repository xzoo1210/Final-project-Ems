package com.ems.api.entity;

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
import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "TASK")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_SEQ")
    @SequenceGenerator(sequenceName = "TASK_SEQ", allocationSize = 1, name = "TASK_SEQ")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable = false, updatable = false)
    private Event event;
    private Long eventId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignerId", insertable = false, updatable = false)
    private Contact assigner;
    private Long assignerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigneeId", insertable = false, updatable = false)
    private Contact assignee;
    private Long assigneeId;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate = new Date();
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartDate;
    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualEndDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentTaskId", insertable = false, updatable = false)
    private Task parentTask;
    private Long parentTaskId;
    @OneToMany
    @JoinColumn(name = "taskId", insertable = false, updatable = false)
    @JsonIgnore
    private Collection<TaskComment> taskComments;
    @OneToMany
    @JoinColumn(name = "parentTaskId", insertable = false, updatable = false)
    @JsonIgnore
    private List<Task> childrenTask;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer progress;
    private String code;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.TaskStatus status = Constant.TypeConstant.TaskStatus.OPEN;
}
