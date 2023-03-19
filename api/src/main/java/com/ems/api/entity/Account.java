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
@Table(name = "ACCOUNT")
public class Account implements Serializable {
    @Id
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String role;
    @Enumerated(EnumType.STRING)
    private Constant.TypeConstant.AccountStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

}
