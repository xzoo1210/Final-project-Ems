package com.ems.api.service.impl;

import com.ems.api.dto.response.TeamContactResponse;
import com.ems.api.repository.ContactRepo;
import com.ems.api.repository.TeamContactRepo;
import com.ems.api.repository.UserContactRepo;
import com.ems.api.service.TeamService;
import com.ems.api.util.AppException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamContactRepo teamContactRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserContactRepo userContactRepo;
    @Autowired
    private ContactRepo contactRepo;

    @Override
    public List<TeamContactResponse> loadAllMember(Long teamId, String currentUserEmail) throws AppException {

        return teamContactRepo.findAllByTeamId(teamId).stream()
                .map(tc -> {
                    TeamContactResponse tcr = modelMapper.map(tc, TeamContactResponse.class);

                    tcr.getMember().setIsAdded(userContactRepo
                            .existsByUserIdAndOtherContactId(contactRepo
                                    .findByAccountEmail(currentUserEmail).getId(),
                                    tcr.getMember().getId()));

                    return tcr;
                }).collect(Collectors.toList());
    }
}
