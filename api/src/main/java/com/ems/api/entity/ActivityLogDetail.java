package com.ems.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "ACTIVITY_LOG_DETAIL")
@NoArgsConstructor
public class ActivityLogDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITY_LOG_DETAIL_SEQ")
    @SequenceGenerator(sequenceName = "ACTIVITY_LOG_DETAIL_SEQ", allocationSize = 1, name = "ACTIVITY_LOG_DETAIL_SEQ")
    private Long id;
    private Long activityLogId;
    private String property;
    @Column(columnDefinition = "TEXT")
    private String oldValue;
    @Column(columnDefinition = "TEXT")
    private String newValue;

    public ActivityLogDetail(Long activityLogId, String property, String oldValue, String newValue) {
        this.activityLogId = activityLogId;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
