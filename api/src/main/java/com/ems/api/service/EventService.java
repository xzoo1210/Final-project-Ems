package com.ems.api.service;

import com.ems.api.dto.request.ChangeEventStatusRequest;
import com.ems.api.dto.request.UpdateMailTemplate;
import com.ems.api.entity.Event;
import com.ems.api.dto.request.CreateEventRequest;
import com.ems.api.dto.request.SearchEventRequest;
import com.ems.api.dto.response.EventResponse;
import com.ems.api.dto.response.SearchEventResponse;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {
    Event create(CreateEventRequest input, String creator) throws AppException;

    Event update(CreateEventRequest input) throws AppException;

    SearchEventResponse<EventResponse> search(SearchEventRequest request, String user) ;

    EventResponse getById(Long id);

    Event findByCode(String code);

    List<EventResponse> getAllJoinedEventsShort(String creator);

    List<Event> getAllJoinedEvents(String creator);

    EventResponse updateContentMail(UpdateMailTemplate request, Constant.EmailTemplate template) throws AppException;

    EventResponse changeEventStatus(ChangeEventStatusRequest request, String userEmail) throws AppException;

    ResponseEntity exportEventInformation(Long eventId) throws AppException;
}
