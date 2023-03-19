package com.ems.api.dto.response;

import com.ems.api.util.Constant;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothResponse {
    private Long id;
    private ContactShortResponse creator;
    private Date createDate;
    @JsonManagedReference("boothRes")
    private EventShortResponse event;
    private Double rentFee;
    private Constant.TypeConstant.BoothStatus status;
    private BoothLocationResponse location;
    private String name;
    private String description;
    private Date startRentDate;
    private Date endRentDate;
    private Boolean isRented;
}
