package com.ems.api.service.impl;

import com.ems.api.dto.request.*;
import com.ems.api.dto.response.ContactShortResponse;
import com.ems.api.dto.response.EventContactResponse;
import com.ems.api.dto.response.EventResponse;
import com.ems.api.entity.Contact;
import com.ems.api.entity.Event;
import com.ems.api.entity.EventContact;
import com.ems.api.repository.*;
import com.ems.api.service.EmailService;
import com.ems.api.service.EventContactService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.Constant.EmailVariable;
import com.ems.api.util.Constant.ErrorCode;
import com.ems.api.util.Constant.TypeConstant.EventFeature;
import com.ems.api.util.Constant.TypeConstant.EventMemberAccess;
import com.ems.api.util.Constant.TypeConstant.EventMemberStatus;
import com.ems.api.util.JwtUtil;
import com.ems.api.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ems.api.util.Constant.TypeConstant.EventMemberStatus.WAITING;

@Service
@Transactional(rollbackFor = {AppException.class, Exception.class})
public class EventContactServiceImpl implements EventContactService {
    @Autowired
    private EventContactRepo eventContactRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private EmailService emailService;
    @Value("${server.api}")
    private String domainApi;
    @Value("${server.web}")
    private String domainWeb;
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    private TeamContactRepo teamContactRepo;

    @Override
    @Transactional(rollbackFor = {AppException.class, Exception.class})
    public EventContact addMemberToEvent(EventMemberRequest input, String creatorEmail) throws AppException {
        Contact userContact = contactRepo.findByAccountEmail(creatorEmail);
        EventContact newEventMember = modelMapper.map(input, EventContact.class);
        Optional<EventContact> optExistEventContact = eventContactRepo.findByEventIdAndMemberId(input.getEventId(), input.getMemberId());
        boolean flagReInvite = false;
        if (optExistEventContact.isPresent()) {
            switch (optExistEventContact.get().getStatus()) {
                case WAITING:
                    throw new AppException(Constant.ErrorCode.MEMBER_IS_INVITED_TO_THIS_EVENT.name());
                case ACCEPT:
                    throw new AppException(Constant.ErrorCode.MEMBER_IS_JOINED_THIS_EVENT.name());
                case REJECT:
                    newEventMember.setId(optExistEventContact.get().getId());
                    flagReInvite = true;
                    break;
                default:
                    throw new AppException(Constant.ErrorCode.SYSTEM_ERROR_INVALID_DATA.name());
            }
        }
        if (input.getIsSubHeader() != null && input.getIsSubHeader()) {
            newEventMember.setEventRole(Constant.TypeConstant.EventRole.SUB_HEADER);
        } else {
            newEventMember.setEventRole(Constant.TypeConstant.EventRole.MEMBER);
        }
        newEventMember.setCreatorId(userContact.getId());
        newEventMember.setStatus(WAITING);

        Contact memberContact = contactRepo.getOne(input.getMemberId());
        Event event = eventRepo.getOne(input.getEventId());
        //TODO send mail here
        Map<String, Object> claims = new HashMap<>();
        //mail re invite
        claims.put(JwtUtil.CLAIM_REINVITE, flagReInvite);
        claims.put(JwtUtil.CLAIM_CONTACTID, memberContact.getId());
        claims.put(JwtUtil.CLAIM_EVENTID, input.getEventId());
        String token = JwtUtil.createToken(claims);
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setTo(Collections.singletonList(memberContact.getEmailAccount()));
        sendMailRequest.setEmailType(Constant.EmailType.INVITE_EVENT);
        Map<String, Object> rawData = new HashMap<>();
        rawData.put(EmailVariable.RECEIVER_NAME.value, memberContact.getFirstName());
        rawData.put(EmailVariable.EVENT_NAME.value, event.getName());
        rawData.put(EmailVariable.EVENT_CODE.value, event.getCode());
        rawData.put(EmailVariable.EVENT_DESCRIPTION.value, event.getDescription());
        rawData.put(EmailVariable.SENDER_NAME.value, userContact.getFirstName());
        rawData.put(EmailVariable.LOGO_URL.value, StringUtils.nvl(event.getLogoPath(),
                "https://res.cloudinary.com/dcgjui0yd/image/upload/v1671031958/logo_ixfins.png"));
        rawData.put(EmailVariable.ACCEPT_EVENT_INVITE.value, domainApi + Constant.ControllerMapping.EVENT_CONTACT + "/accept-event-request/" + token);
        rawData.put(EmailVariable.REJECT_EVENT_INVITE.value, domainApi + Constant.ControllerMapping.EVENT_CONTACT + "/reject-event-request/" + token);
        sendMailRequest.setRawData(rawData);
        emailService.composeEmail(Collections.singletonList(sendMailRequest));
        return eventContactRepo.save(newEventMember);
    }

    @Override
    public void checkAuthorInEvent(Long eventId, String userEmail, Constant.TypeConstant.EventFeature feature, Constant.TypeConstant.EventMemberAccess accessCheck) throws AppException {
        if (!eventRepo.existsById(eventId)) {
            throw new AppException(Constant.ErrorCode.EVENT_NOT_EXIST.name());
        }
        Contact userContact = contactRepo.findByAccountEmail(userEmail);
        Optional<EventContact> optUserEventContact = eventContactRepo.findByEventIdAndMemberId(eventId, userContact.getId());
        if (!optUserEventContact.isPresent()) {
            throw new AppException(Constant.ErrorCode.CURRENT_USER_NOT_IN_THIS_EVENT.name());
        }
        EventContact userEventContact = optUserEventContact.get();
        if (!Constant.TypeConstant.EventMemberStatus.ACCEPT.equals(userEventContact.getStatus())) {
            throw new AppException(Constant.ErrorCode.CURRENT_USER_NOT_ACCEPT_TO_JOIN_THIS_EVENT_YET.name());
        }
        switch (userEventContact.getEventRole()) {
            case HEADER:
                break;
            case SUB_HEADER:
                if (feature.equals(Constant.TypeConstant.EventFeature.EVENT_DETAIL)
                        && accessCheck.equals(EventMemberAccess.EDIT))
                    throw new AppException(Constant.ErrorCode.CURRENT_USER_NOT_HAS_AUTHOR.name()+"_" + accessCheck.name()+"_" + Constant.TypeConstant.EventFeature.EVENT_DETAIL.name());
                break;
            case MEMBER:
                switch (feature) {
                    case POST:
                        checkAccess(userEventContact.getPostAccess(), accessCheck, EventFeature.POST);
                        break;
                    case ORGANIZATION:
                        checkAccess(userEventContact.getOrganizationAccess(), accessCheck, EventFeature.ORGANIZATION);
                        break;
                    case BOOTH:
                        checkAccess(userEventContact.getBoothAccess(), accessCheck, EventFeature.BOOTH);
                        break;
                    case AGENDA:
                        checkAccess(userEventContact.getAgendaAccess(), accessCheck, EventFeature.AGENDA);
                        break;
                    case TICKET:
                        checkAccess(userEventContact.getTicketAccess(), accessCheck, EventFeature.TICKET);
                        break;
                    case SPONSOR:
                        checkAccess(userEventContact.getSponsorAccess(), accessCheck, EventFeature.SPONSOR);
                        break;
                    case PARTICIPANT:
                        checkAccess(userEventContact.getParticipantAccess(), accessCheck, EventFeature.PARTICIPANT);
                        break;
                    case EVENT_DETAIL:
                        if (feature.equals(Constant.TypeConstant.EventFeature.EVENT_DETAIL)
                                && accessCheck.equals(EventMemberAccess.EDIT))
                            throw new AppException(Constant.ErrorCode.CURRENT_USER_NOT_HAS_AUTHOR.name() + accessCheck.name() + Constant.TypeConstant.EventFeature.EVENT_DETAIL.name());
                        break;
                }
                break;
            default:
                throw new AppException(Constant.ErrorCode.SYSTEM_ERROR_INVALID_DATA.name());
        }

    }

    private void checkAccess(EventMemberAccess access, EventMemberAccess accessCheck, EventFeature feature) throws AppException {
        switch (access) {
            case EDIT:
                return;
            case VIEW:
                if (EventMemberAccess.EDIT.equals(accessCheck)) {
                    throw new AppException(ErrorCode.CURRENT_USER_NOT_HAS_AUTHOR + "_" + accessCheck + "_" + feature);
                }
                break;
            case NONE:
                throw new AppException(ErrorCode.CURRENT_USER_NOT_HAS_AUTHOR + "_" + feature);
        }
    }

    @Override
    public EventContact findByEventAndCurrentUser(Long eventId, String userEmail) {
        Contact userContact = contactRepo.findByAccountEmail(userEmail);
        return eventContactRepo.findByEventIdAndMemberId(eventId, userContact.getId()).orElse(null);
    }

    @Override
    public List<EventContactResponse> loadAllMember(Long eventId, EventMemberStatus status, String userEmail) {
        Contact userContact = contactRepo.findByAccountEmail(userEmail);
        Set<Long> addedContactId = userContact.getAddedUserContacts().stream().map(a -> a.getOtherContactId()).collect(Collectors.toSet());

        return eventContactRepo.findAllByEventIdAndStatus(eventId, status).stream()
                .map(ec -> {
                    EventContactResponse ecr = modelMapper.map(ec, EventContactResponse.class);
                    ecr.setMember(modelMapper.map(ec.getMember(), ContactShortResponse.class));
                    ecr.getMember().setIsAdded(addedContactId.contains(ecr.getMember().getId()));
                    ecr.setCreator(modelMapper.map(ec.getCreator(), ContactShortResponse.class));
                    ecr.setEvent(modelMapper.map(ec.getEvent(), EventResponse.class));
//                    ecr.setNoLateTask(taskRepo.findAllByEventIdAndAssigneeIdAndEndDateBefore(eventId,ec.getMemberId(),new Date()).size());
//                    teamContactRepo.findAllByMemberId()
                    return ecr;
                }).collect(Collectors.toList());
    }

    //    @Override
//    public EventContactResponse findByEventAndCurrentUser(Long eventId, String userEmail) {
//        Contact userContact = contactRepo.findByAccountEmail(userEmail);
//        eventContactRepo.findByEventIdAndMemberId(eventId, userContact.getId());
//        return null;
//    }
    @Override
    public void changeStatusEventMemberRequest(ChangeEventRequest changeEventRequest, EventMemberStatus status) {
        Optional<EventContact> optUserEventContact = eventContactRepo.findByEventIdAndMemberId(changeEventRequest.getEventId(), changeEventRequest.getMemberId());
        if (optUserEventContact.isPresent()) {
            EventContact ec = optUserEventContact.get();
            if (WAITING.equals(ec.getStatus()))
                ec.setStatus(status);
        }
    }

    @Override
    public String changeStatusEventMemberRequestWithToken(String token, EventMemberStatus status) {
        Map<String, Object> claims = JwtUtil.extractAllClaims(token);
        Long memberId = Long.valueOf((Integer) claims.get(JwtUtil.CLAIM_CONTACTID));
        Long eventId = Long.valueOf((Integer) claims.get(JwtUtil.CLAIM_EVENTID));
        Optional<EventContact> optUserEventContact = eventContactRepo.findByEventIdAndMemberId(eventId, memberId);
        if (optUserEventContact.isPresent()) {
            EventContact ec = optUserEventContact.get();
            boolean flagChangeStatus = false;
            if (WAITING.equals(ec.getStatus())) {
                ec.setStatus(status);
                eventContactRepo.save(ec);
                flagChangeStatus = true;
            }
            if (status.equals(EventMemberStatus.ACCEPT)) {
//                return domainWeb + "/event-manage/detail/" + eventId;
                return flagChangeStatus ? domainWeb + "/accept-join-event" :
                        domainWeb + "/event-manage/detail/" + ec.getEventId();
            } else {
                return domainWeb + "/reject-join-event";
            }
        } else {
            return domainWeb + "/reject-join-event";
        }


    }

    @Override
    public EventContactResponse getById(Long id) {
        return modelMapper.map(eventContactRepo.getOne(id), EventContactResponse.class);
    }

    @Override
    public List<ContactShortResponse> searchUserToAddToEvent(SearchUserToAddToEvent request, String currentUserEmail) throws AppException {
        Contact currentUserContact = contactRepo.findByAccountEmail(currentUserEmail);
        Map<Long, EventContact> mapContactIdToStatusInEvent = new HashMap<>();
        Collection<EventContact> members = eventRepo.getOne(request.getEventId()).getEventContacts();
        if (!members.isEmpty()) {
            for (EventContact ec : members) {
                mapContactIdToStatusInEvent.put(ec.getMemberId(), ec);
            }
        }
        List<Contact> contactPage = contactRepo.searchUserToAddToEvent(request.getKeyword(), currentUserContact.getId(),
                Constant.TypeConstant.ContactType.INDIVIDUAL);

        List<ContactShortResponse> foundContacts = new ArrayList<>();
        contactPage.stream()
                .forEach(fc -> {
                    ContactShortResponse csr = modelMapper.map(fc, ContactShortResponse.class);
                    if (mapContactIdToStatusInEvent.containsKey(fc.getId())) {
                        EventContact ec = mapContactIdToStatusInEvent.get(fc.getId());
                        if (EventMemberStatus.REJECT.equals(ec.getStatus())) {
                            csr.setStatusInEvent(ec.getStatus());
                            csr.setRoleInEvent(ec.getEventRole());
                            foundContacts.add(csr);
                        }
                    } else {
                        foundContacts.add(csr);
                    }
                });
        return foundContacts;
    }

    @Override
    public EventContact updateMember(UpdateEventMemberRequest input) {
        EventContact eventContact = eventContactRepo.getOne(input.getId());
        eventContact.setOrganizationAccess(input.getOrganizationAccess());
        eventContact.setAgendaAccess(input.getAgendaAccess());
        eventContact.setTicketAccess(input.getTicketAccess());
        eventContact.setBoothAccess(input.getBoothAccess());
        eventContact.setSponsorAccess(input.getSponsorAccess());
        eventContact.setPostAccess(input.getPostAccess());
        eventContact.setParticipantAccess(input.getParticipantAccess());
        if (input.getIsSubHeader() != null) {
            if (input.getIsSubHeader()) {
                eventContact.setEventRole(Constant.TypeConstant.EventRole.SUB_HEADER);
            } else {
                eventContact.setEventRole(Constant.TypeConstant.EventRole.MEMBER);
            }
        }

        return eventContactRepo.save(eventContact);
    }

    @Override
    public EventContact deleteMember(Long eventId, Long memberId) {
        EventContact eventContact = eventContactRepo.findByEventIdAndMemberId(eventId, memberId).get();
        eventContactRepo.delete(eventContact);
        return eventContact;
    }


}
