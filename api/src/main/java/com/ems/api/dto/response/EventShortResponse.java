package com.ems.api.dto.response;

import com.ems.api.util.Constant;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EventShortResponse {
    private Long id;
    private String name;
    private String code;
    private String logoPath;
    private Constant.TypeConstant.EventStatus status;
}
