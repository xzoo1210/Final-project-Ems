package com.ems.api.dto.response;

import com.ems.api.util.Constant;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactShortResponse {
    private Long id;
    private String emailAccount;
    private String imagePath;
    private String firstName;
    private Boolean isAdded;
    private Constant.TypeConstant.EventMemberStatus statusInEvent;
    private Constant.TypeConstant.EventRole roleInEvent;
}
