package com.ems.api.dto.response;

import com.ems.api.util.Constant;
import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemberResponse {
    private Long id;
    private ContactShortResponse member;
    private Date createDate;
    private Constant.TypeConstant.TeamRole teamRole;
}
