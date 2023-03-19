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
@Table(name = "TASK_COMMENT")
public class TaskComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_COMMENT_SEQ")
    @SequenceGenerator(sequenceName = "TASK_COMMENT_SEQ", allocationSize = 1, name = "TASK_COMMENT_SEQ")
    private Long id;

    //    @ManyToOne( fetch = FetchType.LAZY)
//    @JoinColumn(name = "taskId",insertable = false,updatable = false)
//    private Task task;
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId", insertable = false, updatable = false)
    private Contact creator;
    private Long creatorId;

    //@JsonFormat(pattern = DateUtil.DATETIME_FORMAT,timezone = "Asia/Ho_Chi_Minh")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate = new Date();

    @Column(columnDefinition = "TEXT")
    private String content;


}
