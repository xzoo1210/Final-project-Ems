package com.ems.api.service;

import com.ems.api.dto.response.TeamContactResponse;
import com.ems.api.util.AppException;

import java.util.List;

public interface TeamService {
    List<TeamContactResponse>  loadAllMember(Long teamId, String currentUserEmail) throws AppException;
}
