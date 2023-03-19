package com.ems.api.service;

import com.ems.api.entity.Contact;
import com.ems.api.entity.TeamContact;
import com.ems.api.entity.UserContact;
import com.ems.api.dto.request.*;
import com.ems.api.dto.response.ContactShortResponse;
import com.ems.api.dto.response.SearchContactResponse;
import com.ems.api.dto.response.TeamResponse;
import com.ems.api.dto.response.TeamShortResponse;
import com.ems.api.util.AppException;

import java.util.List;


public interface ContactService {
    Contact createContact(CreateContactRequest input, String creator) throws AppException;

    Contact updateContact(UpdateContactRequest input) throws AppException;

    Contact createTeam(TeamRequest input, String creator) throws AppException;

    Contact updateProfile(UpdateProfileRequest input) throws AppException;

    SearchContactResponse searchContact(SearchContactRequest request, String currentUserEmail) throws AppException;

    Contact loadProfileByAccountEmail(String email) throws AppException;

    UserContact removeOtherUser(Long input, String userEmail) throws AppException;

    UserContact addOtherUser(Long otherId, String currentUserEmail) throws AppException;

    TeamContact addOtherUserToTeam(Long userId, Long teamId, String currentUserEmail) throws AppException;

    Contact getById(Long id);

    ContactShortResponse getShortContactById(Long id);

    TeamResponse getTeamById(Long id, String currentUserEmail);

    List<TeamShortResponse> findByEventId(Long eventId, String creator);

    List<TeamShortResponse> findAllByEventId(Long eventId);

    Contact updateTeam(TeamUpdateRequest input);

    List<Contact> getAddedContactUsers(String user);

    List<ContactShortResponse> searchUserToAddToTeam(SearchUserToAddToTeam input, String currentUser) throws AppException;

    TeamContact removeUserFromTeam(Long userToRemoveId, Long teamId) throws AppException;

    TeamContact grantLeaderRole(Long userId, Long teamId) throws AppException;
}
