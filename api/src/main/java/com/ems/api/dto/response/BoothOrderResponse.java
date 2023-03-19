package com.ems.api.dto.response;

import com.ems.api.util.Constant;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothOrderResponse {
    private String name;
    private String email;
    private String phone;
    private BoothResponse booth;
    private Constant.TypeConstant.BoothOrderStatus status;
    private Date createDate;
    private Long id;
}
