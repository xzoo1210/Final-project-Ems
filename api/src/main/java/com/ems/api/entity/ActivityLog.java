package com.ems.api.entity;

import com.ems.api.util.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "ACTIVITY_LOG")
@NoArgsConstructor
public class ActivityLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITY_LOG_SEQ")
    @SequenceGenerator(sequenceName = "ACTIVITY_LOG_SEQ", allocationSize = 1, name = "ACTIVITY_LOG_SEQ")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate = new Date();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actorId", insertable = false, updatable = false)
    @JsonIgnore
    private Contact actor;
    private Long actorId;
    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.ActivityType activityType;
    private Long featureId;
    @Column(columnDefinition = "TEXT")
    private String note;
    @OneToMany
    @JoinColumn(name = "activityLogId", insertable = false, updatable = false)
    private List<ActivityLogDetail> activityLogDetails;

    public ActivityLog(Long actorId, Constant.TypeConstant.ActivityType activityType, Long featureId, String note) {
        this.actorId = actorId;
        this.activityType = activityType;
        this.featureId = featureId;
        this.note = note;
    }
}
