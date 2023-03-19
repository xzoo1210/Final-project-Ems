package com.ems.api.service.impl;

import com.ems.api.entity.Agenda;
import com.ems.api.dto.request.AgendaRequest;
import com.ems.api.dto.response.AgendaResponse;
import com.ems.api.repository.AgendaRepo;
import com.ems.api.repository.ContactRepo;
import com.ems.api.service.AgendaService;
import com.ems.api.util.AppException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AgendaServiceImpl implements AgendaService {
    @Autowired
    private AgendaRepo agendaRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ContactRepo contactRepo;
    @Override
    public AgendaResponse create(AgendaRequest request, String creator) {
        request.setId(null);
        Agenda agenda = modelMapper.map(request, Agenda.class);
        agenda.setCreatorId(contactRepo.findByAccountEmail(creator).getId());
        return modelMapper.map(agendaRepo.save(agenda), AgendaResponse.class);
    }

    @Override
    public AgendaResponse update(AgendaRequest request) throws AppException {
        Agenda agenda = agendaRepo.getOne(request.getId());
        agenda.setName(request.getName());
        agenda.setDescription(request.getDescription());
        agenda.setStartDate(request.getStartDate());
        agenda.setEndDate(request.getEndDate());
        return modelMapper.map(agendaRepo.save(agenda), AgendaResponse.class);
    }

    @Override
    public List<AgendaResponse> getAllInEvent(Long eventId) {
        return agendaRepo.findAllByEventId(eventId).stream()
                .map(a -> modelMapper.map(a, AgendaResponse.class)).collect(Collectors.toList());
    }

    @Override
    public AgendaResponse getById(Long id) {
        return modelMapper.map(agendaRepo.getOne(id), AgendaResponse.class);
    }

    @Override
    public AgendaResponse delete(Long id) {
        Agenda agenda = agendaRepo.getOne(id);
        agendaRepo.delete(agenda);
        return modelMapper.map(agenda, AgendaResponse.class);
    }
}
