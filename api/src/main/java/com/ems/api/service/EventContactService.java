package com.ems.api.service;

import com.ems.api.entity.EventContact;
import com.ems.api.dto.request.ChangeEventRequest;
import com.ems.api.dto.request.EventMemberRequest;
import com.ems.api.dto.request.SearchUserToAddToEvent;
import com.ems.api.dto.request.UpdateEventMemberRequest;
import com.ems.api.dto.response.ContactShortResponse;
import com.ems.api.dto.response.EventContactResponse;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;

import java.util.List;

public interface EventContactService {
    EventContact addMemberToEvent(EventMemberRequest input, String creatorEmail) throws AppException;

    void checkAuthorInEvent(Long eventId, String creatorEmail, Constant.TypeConstant.EventFeature feature, Constant.TypeConstant.EventMemberAccess access) throws AppException;

    EventContact findByEventAndCurrentUser(Long eventId, String creatorEmail);

    List<EventContactResponse> loadAllMember(Long eventId, Constant.TypeConstant.EventMemberStatus accept, String userEmail);

    void changeStatusEventMemberRequest(ChangeEventRequest changeEventRequest, Constant.TypeConstant.EventMemberStatus status);

    List<ContactShortResponse> searchUserToAddToEvent(SearchUserToAddToEvent searchContactRequest, String creatorEmail) throws AppException;

    EventContact updateMember(UpdateEventMemberRequest input);

    EventContact deleteMember(Long eventId, Long memberId);

    String changeStatusEventMemberRequestWithToken(String token, Constant.TypeConstant.EventMemberStatus accept);

    EventContactResponse getById(Long id);
//    EventContactResponse findByEventAndCurrentUser(Long eventId, String creatorEmail);
}
