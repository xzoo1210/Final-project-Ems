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
@Table(name = "BOOTH_ORDER")
public class
BoothOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOTH_ORDER_SEQ")
    @SequenceGenerator(sequenceName = "BOOTH_ORDER_SEQ", allocationSize = 1, name = "BOOTH_ORDER_SEQ")
    private Long id;

    private String name;
    private String email;
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boothId", insertable = false, updatable = false)
    @JsonIgnore
    private Booth booth;
    private Long boothId;

    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.BoothOrderStatus status;

}
