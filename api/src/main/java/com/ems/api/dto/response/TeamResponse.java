package com.ems.api.dto.response;

import lombok.Data;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamResponse extends TeamShortResponse {
    List<TeamMemberResponse> members;
}
