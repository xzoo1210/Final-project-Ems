package com.ems.api.service;

import com.ems.api.entity.Participant;
import com.ems.api.dto.request.ParticipantRequest;
import com.ems.api.dto.request.SearchParticipantRequest;
import com.ems.api.dto.response.ParticipantResponse;
import com.ems.api.dto.response.SearchResponse;
import com.ems.api.dto.response.TicketOrderResponse;
import com.ems.api.util.AppException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ParticipantService {
    List<Participant> addParticipantFromOrder(TicketOrderResponse orderResponse);

    SearchResponse<ParticipantResponse> search(SearchParticipantRequest request);

    ParticipantResponse inviteParticipant(ParticipantRequest request, String userEmail) throws AppException;

    ParticipantResponse confirmJoined(Long id, String userEmail);

    ResponseEntity getFile(Long eventId) throws AppException;

    void sendTicketToParticipants(List<Participant> participants) throws AppException;
    void sendInviteParticipantMail(Long participantId) throws AppException;
    ParticipantResponse getById(Long id);
}
