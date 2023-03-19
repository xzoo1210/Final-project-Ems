package com.ems.api.service;

import com.ems.api.dto.request.AgendaRequest;
import com.ems.api.dto.response.AgendaResponse;
import com.ems.api.util.AppException;

import java.util.List;

public interface AgendaService {
    AgendaResponse create(AgendaRequest request, String creator) throws AppException;

    AgendaResponse update(AgendaRequest request) throws AppException;

    List<AgendaResponse> getAllInEvent(Long eventId);

    AgendaResponse getById(Long id);
    AgendaResponse delete(Long id);
}
