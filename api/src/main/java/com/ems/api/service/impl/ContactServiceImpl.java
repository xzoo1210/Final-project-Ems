package com.ems.api.service.impl;

import com.ems.api.dto.request.*;
import com.ems.api.dto.response.*;
import com.ems.api.entity.Contact;
import com.ems.api.entity.Event;
import com.ems.api.entity.TeamContact;
import com.ems.api.entity.UserContact;
import com.ems.api.repository.ContactRepo;
import com.ems.api.repository.EventRepo;
import com.ems.api.repository.TeamContactRepo;
import com.ems.api.repository.UserContactRepo;
import com.ems.api.service.AddressService;
import com.ems.api.service.ContactService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.PageUtil;
import com.ems.api.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = AppException.class)
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private TeamContactRepo teamContactRepo;
    @Autowired
    private UserContactRepo userContactRepo;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Contact createContact(CreateContactRequest input, String creator) throws AppException {
        Contact owner = contactRepo.findByAccountEmail(creator);
        Contact newContact = newContactFromCreateRequest(input, owner.getId());
        return contactRepo.save(newContact);
    }

    @Override
    public Contact updateContact(UpdateContactRequest input) throws AppException {
        Contact currentContact = contactRepo.findById(input.getId()).orElse(null);
        if (currentContact == null) {
            throw new AppException(Constant.ErrorCode.CONTACT_ID_NOT_EXIST.name());
        }
        currentContact.setEmail(input.getEmail());
        currentContact.setPhone(input.getPhone());
        currentContact.setMobile(input.getMobile());
        currentContact.setWebsiteLink(input.getWebsiteLink());
        currentContact.setImagePath(input.getImagePath());
        currentContact.setFirstName(input.getFirstName());
        return contactRepo.save(currentContact);
    }

    @Override
    public Contact createTeam(TeamRequest input, String creator) throws AppException {
        Contact currentUser = contactRepo.findByAccountEmail(creator);
        Contact newTeam = newContactFromCreateRequest(input, currentUser.getId());
        newTeam.setContactType(Constant.TypeConstant.ContactType.TEAM);
        Event event = eventRepo.getOne(input.getEventId());
        newTeam.setEventId(input.getEventId());
        newTeam.setDescription(input.getDescription());
        newTeam = contactRepo.save(newTeam);
        //current user
        TeamContact teamContactCurrentUser = new TeamContact();
        teamContactCurrentUser.setTeamId(newTeam.getId());
        teamContactCurrentUser.setMemberId(currentUser.getId());
        teamContactCurrentUser.setTeamRole(Constant.TypeConstant.TeamRole.LEADER);
        teamContactRepo.save(teamContactCurrentUser);
        //event header
        if (!event.getCreatorId().equals(currentUser.getId())){
            TeamContact teamContactEventHeader = new TeamContact();
            teamContactEventHeader.setTeamId(newTeam.getId());
            teamContactEventHeader.setMemberId(event.getCreatorId());
            teamContactEventHeader.setTeamRole(Constant.TypeConstant.TeamRole.LEADER);
            teamContactRepo.save(teamContactEventHeader);
        }
            return newTeam;
    }

    private Contact newContactFromCreateRequest(CreateContactRequest request, Long owner) throws AppException {
        Contact newContact = new Contact();
        if (owner == null)
            throw new AppException(Constant.ErrorCode.OWNER_BY_EMAIL_JWT_NOT_EXIST.name());
        newContact.setId(null);
        newContact.setOwnerId(owner);
        newContact.setEmail(request.getEmail());
        newContact.setPhone(request.getPhone());
        newContact.setMobile(request.getMobile());
        newContact.setWebsiteLink(request.getWebsiteLink());
        newContact.setImagePath(request.getImagePath());
        newContact.setFirstName(request.getFirstName());
        return newContact;
    }

    @Override
    public Contact updateProfile(UpdateProfileRequest input) throws AppException {
        Contact currentProfile = contactRepo.findById(input.getId()).orElse(null);
        if (currentProfile == null) {
            throw new AppException(Constant.ErrorCode.CONTACT_ID_NOT_EXIST.name());
        }
        if (StringUtils.isEmpty(currentProfile.getEmailAccount()) ||
                !Constant.TypeConstant.ContactType.INDIVIDUAL.equals(currentProfile.getContactType())) {
            throw new AppException(Constant.ErrorCode.THIS_CONTACT_IS_NOT_USER_PROFILE.name());
        }
        currentProfile.setEmail(input.getEmail());
        currentProfile.setPhone(input.getPhone());
        currentProfile.setMobile(input.getMobile());
        currentProfile.setWebsiteLink(input.getWebsiteLink());
        currentProfile.setImagePath(input.getImagePath());
        currentProfile.setFirstName(input.getFirstName());
//        Long addressId = null;
//        if (input.getAddressRequest().getId() != null) {
//            addressId = addressService.update(input.getAddressRequest()).getId();
//        } else {
//            addressId = addressService.create(input.getAddressRequest()).getId();
//        }
//        currentProfile.setAddressId(addressId);
        return contactRepo.save(currentProfile);
    }

    @Override
    public UserContact removeOtherUser(Long id, String userEmail) throws AppException {
        Contact currentUser = contactRepo.findByAccountEmail(userEmail);
        UserContact userContactToDelete = userContactRepo.findByUserIdAndOtherContactId(currentUser.getId(), id);
        userContactRepo.delete(userContactToDelete);
        return userContactToDelete;
    }

    @Override
    public SearchContactResponse<Contact> searchContact(SearchContactRequest request, String currentUserEmail) throws AppException {
        SearchContactResponse<Contact> searchContactResponse = new SearchContactResponse();
        Contact currentUserContact = contactRepo.findByAccountEmail(currentUserEmail);
        Map<Long, TeamShortResponse> currentUserJoinedTeams = getJoinedTeams(currentUserContact).stream().collect(Collectors.toMap(TeamShortResponse::getId, Function.identity()));
        Map<Long, EventShortResponse> currentUserJoinedEvents = getJoinedEvents(currentUserContact).stream().collect(Collectors.toMap(EventShortResponse::getId, Function.identity()));

        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Contact> contactPage = null;
        if (request.isFindAddedUser()) {
            contactPage = contactRepo.searchAdded(request.getKeyword(), request.getTeamId(), request.getEventId(),
                    currentUserContact.getId(), pageableRequested);
        } else {
            contactPage = contactRepo.searchNotAdded(request.getKeyword(), currentUserContact.getId(), pageableRequested);
        }
        List<Contact> foundContacts = contactPage.getContent();
        foundContacts.stream().forEach(fc -> {
            fc.setTeamsInCommon(getTeamInCommon(fc, currentUserJoinedTeams));
            fc.setEventsInCommon(getEventInCommon(fc, currentUserJoinedEvents));
        });
        searchContactResponse.setContactPage(new PageImpl<>(foundContacts, pageableRequested, contactPage.getTotalElements()));
        return searchContactResponse;
    }

    @Override
    public Contact loadProfileByAccountEmail(String email) throws AppException {
        return contactRepo.findByAccountEmail(email);
    }

    @Override
    public UserContact addOtherUser(Long otherId, String currentUserEmail) throws AppException {
        Contact currentUser = contactRepo.findByAccountEmail(currentUserEmail);
        if (!contactRepo.findById(otherId).isPresent()) {
            throw new AppException(Constant.ErrorCode.OTHER_CONTACT_ID_NOT_EXIST.name());
        }
        if (userContactRepo.existsByUserIdAndOtherContactId(currentUser.getId(), otherId))
            throw new AppException(Constant.ErrorCode.OTHER_CONTACT_IS_ALREADY_ADDED.name());
        UserContact userContact = new UserContact();
        userContact.setUserId(currentUser.getId());
        userContact.setOtherContactId(otherId);
        return userContactRepo.save(userContact);
    }

    @Override
    public TeamContact addOtherUserToTeam(Long userId, Long teamId, String currentUserEmail) throws AppException {
        TeamContact teamContact = new TeamContact();
        teamContact.setTeamId(teamId);
        teamContact.setMemberId(userId);
        teamContact.setTeamRole(Constant.TypeConstant.TeamRole.MEMBER);
        return teamContactRepo.save(teamContact);
    }

    @Override
    public Contact getById(Long id) {
        return contactRepo.findByIdAndContactType(id, Constant.TypeConstant.ContactType.INDIVIDUAL);
    }

    @Override
    public ContactShortResponse getShortContactById(Long id) {
        return modelMapper.map(contactRepo.findByIdAndContactType(id, Constant.TypeConstant.ContactType.INDIVIDUAL), ContactShortResponse.class);
    }

    @Override
    public TeamResponse getTeamById(Long id, String currentUserEmail) {
        Contact team = contactRepo.findByIdAndContactType(id, Constant.TypeConstant.ContactType.TEAM);
        if (team == null)
            return null;
        Contact currentUserContact = contactRepo.findByAccountEmail(currentUserEmail);
        TeamResponse teamResponse = modelMapper.map(team, TeamResponse.class);
        teamResponse.setMembers(team.getTeamMembers().stream().map(
                tm -> {
                    TeamMemberResponse tmr = new TeamMemberResponse();
                    tmr.setId(tm.getId());
                    tmr.setCreateDate(tm.getCreateDate());
                    tmr.setTeamRole(tm.getTeamRole());
                    tmr.setMember(modelMapper.map(tm.getMember(), ContactShortResponse.class));

                    tmr.getMember().setIsAdded(userIsAdded(tmr.getMember().getId(), currentUserContact.getId()));
                    return tmr;
                }
        ).collect(Collectors.toList()));
        teamResponse.setCreator(modelMapper.map(team.getOwnerContact(), ContactShortResponse.class));
        return teamResponse;
    }

    @Override
    public List<TeamShortResponse> findByEventId(Long eventId, String creator) {
        return contactRepo.findAllByEventIdAndContainUser(eventId, creator).stream().map(
                c -> {
                    TeamShortResponse tsr = modelMapper.map(c, TeamShortResponse.class);
                    tsr.setCreator(modelMapper.map(c.getOwnerContact(), ContactShortResponse.class));
                    tsr.setNoMembers(c.getTeamMembers().size());
                    return tsr;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<TeamShortResponse> findAllByEventId(Long eventId) {
        return contactRepo.findAllByEventId(eventId).stream().map(
                c -> {
                    TeamShortResponse tsr = modelMapper.map(c, TeamShortResponse.class);
                    tsr.setCreator(modelMapper.map(c.getOwnerContact(), ContactShortResponse.class));
                    tsr.setNoMembers(c.getTeamMembers().size());
                    return tsr;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public Contact updateTeam(TeamUpdateRequest input) {
        Contact team = contactRepo.getOne(input.getId());
        team.setDescription(input.getDescription());
        team.setEmail(input.getEmail());
        team.setPhone(input.getPhone());
        team.setMobile(input.getMobile());
        team.setWebsiteLink(input.getWebsiteLink());
        team.setImagePath(input.getImagePath());
        team.setFirstName(input.getFirstName());
        return contactRepo.save(team);
    }

    @Override
    public List<Contact> getAddedContactUsers(String userEmail) {
        Contact user = contactRepo.findByAccountEmail(userEmail);
        return user.getAddedUserContacts() != null && !user.getAddedUserContacts().isEmpty() ?
                user.getAddedUserContacts().stream().map(uc -> uc.getOtherContact()).collect(Collectors.toList()) : Collections.emptyList();
    }

    @Override
    public List<ContactShortResponse> searchUserToAddToTeam(SearchUserToAddToTeam input, String currentUserEmail) {
        Contact team = contactRepo.getOne(input.getTeamId());
        Contact currentUserContact = contactRepo.findByAccountEmail(currentUserEmail);
        return contactRepo.searchUserToAddToTeam(input.getKeyword(),
                currentUserEmail, input.getTeamId(), team.getEventId(), Constant.TypeConstant.ContactType.INDIVIDUAL,
                Constant.TypeConstant.EventMemberStatus.ACCEPT).stream().map(
                fc -> {
                    ContactShortResponse csr = modelMapper.map(fc, ContactShortResponse.class);
                    if (StringUtils.isEmpty(fc.getEmailAccount())) {
                        csr.setIsAdded(true);
                    } else {
                        csr.setIsAdded(userIsAdded(fc.getId(), currentUserContact.getId()));
                    }
                    return csr;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public TeamContact removeUserFromTeam(Long userToRemoveId, Long teamId) {
        TeamContact teamContact = teamContactRepo.findByMemberIdAndTeamId(userToRemoveId, teamId);
        teamContactRepo.delete(teamContact);
        return teamContact;
    }

    @Override
    public TeamContact grantLeaderRole(Long userId, Long teamId) throws AppException {
        TeamContact teamContact = teamContactRepo.findByMemberIdAndTeamId(userId, teamId);
        teamContact.setTeamRole(Constant.TypeConstant.TeamRole.LEADER);
        return teamContactRepo.save(teamContact);
    }

    private Collection<TeamShortResponse> getTeamInCommon(Contact searchedUser, Map<Long, TeamShortResponse> loggedUserJoinedTeams) {
        if (loggedUserJoinedTeams.isEmpty())
            return Collections.emptyList();
//        List<TeamShortResponse> cloneLoggedUserJoinedTeams = new ArrayList<>(loggedUserJoinedTeams);
        Set<Long> searchedUserJoinedTeamsId = getJoinedTeams(searchedUser).stream().map(t -> t.getId()).collect(Collectors.toSet());
        if (searchedUserJoinedTeamsId.isEmpty())
            return Collections.emptyList();
//        cloneLoggedUserJoinedTeams.retainAll(searchedUserJoinedTeams);
        return searchedUserJoinedTeamsId.stream().filter(id -> loggedUserJoinedTeams.containsKey(id))
                .map(id -> loggedUserJoinedTeams.get(id)).collect(Collectors.toList());
    }

    private Collection<EventShortResponse> getEventInCommon(Contact searchedUser, Map<Long, EventShortResponse> loggedUserJoinedEvents) {
        if (loggedUserJoinedEvents.isEmpty())
            return Collections.emptyList();
//        List<EventResponse> cloneLoggedUserJoinedEvents = new ArrayList<>(loggedUserJoinedEvents);
        Set<Long> searchedUserJoinedEventsId = getJoinedEvents(searchedUser).stream().map(t -> t.getId()).collect(Collectors.toSet());
        if (searchedUserJoinedEventsId.isEmpty())
            return Collections.emptyList();
//        cloneLoggedUserJoinedEvents.retainAll(searchedUserJoinedEvents);
        return searchedUserJoinedEventsId.stream().filter(id -> loggedUserJoinedEvents.containsKey(id))
                .map(id -> loggedUserJoinedEvents.get(id)).collect(Collectors.toList());
    }

    private boolean userIsAdded(Long otherId, Long userId) {
        return userContactRepo.existsByUserIdAndOtherContactId(userId, otherId);
    }

    private List<TeamShortResponse> getJoinedTeams(Contact userContact) {
        return userContact.getJoinedTeams() != null && !userContact.getJoinedTeams().isEmpty() ?
                userContact.getJoinedTeams().stream().map(tc -> modelMapper.map(tc.getTeam(), TeamShortResponse.class))
                        .collect(Collectors.toList()) : Collections.emptyList();
    }

    private List<EventShortResponse> getJoinedEvents(Contact userContact) {
        return userContact.getJoinedEvents() != null && !userContact.getJoinedEvents().isEmpty() ?
                userContact.getJoinedEvents().stream().filter(ec -> ec.getStatus() == Constant.TypeConstant.EventMemberStatus.ACCEPT)
                        .map(tc -> modelMapper.map(tc.getEvent(), EventShortResponse.class)).collect(Collectors.toList()) : Collections.emptyList();
    }

}
