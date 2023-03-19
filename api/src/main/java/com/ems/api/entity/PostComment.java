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
@Table(name = "POST_COMMENT")
public class PostComment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_COMMENT_SEQ")
    @SequenceGenerator(sequenceName = "POST_COMMENT_SEQ", allocationSize = 1, name = "POST_COMMENT_SEQ")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creatorId", updatable = false, insertable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Contact creator;

    private Long creatorId;

    private Long postId;

    @Column(columnDefinition = "TEXT")
    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();
}
