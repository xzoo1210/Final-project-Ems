package com.ems.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "ADDRESS")
public class Address implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ADDRESS_SEQ")
    @SequenceGenerator(sequenceName = "ADDRESS_SEQ", allocationSize = 1, name = "ADDRESS_SEQ")
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String street1;
    @Column(columnDefinition = "TEXT")
    private String street2;
    private String province;
    private String district;
    private String commune;

}
