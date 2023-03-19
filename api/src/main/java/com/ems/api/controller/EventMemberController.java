package com.ems.api.controller;

import com.ems.api.entity.EventContact;
import com.ems.api.dto.request.ChangeEventRequest;
import com.ems.api.dto.request.EventMemberRequest;
import com.ems.api.dto.request.SearchUserToAddToEvent;
import com.ems.api.dto.request.UpdateEventMemberRequest;
import com.ems.api.dto.response.ContactShortResponse;
import com.ems.api.dto.response.EventContactResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.service.EventContactService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.EVENT_CONTACT)
public class EventMemberController {
    protected final Logger LOGGER = LoggerFactory.getLogger(EventMemberController.class);
    @Autowired
    private EventContactService eventContactService;

    @RequestMapping(value = {"/add-member"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventContact>> addMember(HttpServletRequest request,
                                                                    @RequestBody EventMemberRequest input) {
        String methodName = "addMember";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), creatorEmail,
                    Constant.TypeConstant.EventFeature.ORGANIZATION, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventContactService.addMemberToEvent(input, creatorEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/update-member"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventContact>> updateMember(HttpServletRequest request,
                                                                       @RequestBody UpdateEventMemberRequest input) {
        String methodName = "updateMember";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), userEmail,
                    Constant.TypeConstant.EventFeature.ORGANIZATION, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventContactService.updateMember(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"{eventId}/delete-member/{memberId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventContact>> deleteMember(@PathVariable Long eventId,
                                                                       @PathVariable Long memberId
    ) {
        String methodName = "deleteMember";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, userEmail,
                    Constant.TypeConstant.EventFeature.ORGANIZATION, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventContactService.deleteMember(eventId, memberId));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/load-all-member/{eventId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<EventContactResponse>>> loadAllMember(HttpServletRequest request,
                                                                                      @PathVariable Long eventId) {
        String methodName = "loadAllMember";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, userEmail, Constant.TypeConstant.EventFeature.ORGANIZATION,
                    Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(eventContactService.loadAllMember(eventId,
                    Constant.TypeConstant.EventMemberStatus.ACCEPT, userEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/load-all-member-waiting/{eventId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<EventContactResponse>>> loadAllMemberWaiting(HttpServletRequest request,
                                                                                             @PathVariable Long eventId) {
        String methodName = "loadAllMemberWaiting";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, userEmail, Constant.TypeConstant.EventFeature.ORGANIZATION,
                    Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(eventContactService.loadAllMember(eventId,
                    Constant.TypeConstant.EventMemberStatus.WAITING, userEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/load-all-member-reject/{eventId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<EventContactResponse>>> loadAllMemberReject(HttpServletRequest request,
                                                                                            @PathVariable Long eventId) {
        String methodName = "loadAllMemberReject";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, userEmail, Constant.TypeConstant.EventFeature.ORGANIZATION,
                    Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(eventContactService.loadAllMember(eventId,
                    Constant.TypeConstant.EventMemberStatus.REJECT, userEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/load-user-prop/{eventId}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventContact>> loadUserProp(HttpServletRequest request,
                                                                       @PathVariable Long eventId) {
        String methodName = "loadUserProp";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        LOGGER.info(request.getLocalAddr());
        LOGGER.info(request.getRemoteAddr());
        MessagesResponse messagesResponse = new MessagesResponse();
        String creatorEmail =
                SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(eventContactService.findByEventAndCurrentUser(eventId, creatorEmail));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/accept-event-request"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse> acceptEventRequest(HttpServletRequest request,
                                                               @RequestBody ChangeEventRequest changeEventRequest) {
        String methodName = "acceptEventRequest";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        eventContactService.changeStatusEventMemberRequest(changeEventRequest, Constant.TypeConstant.EventMemberStatus.ACCEPT);
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/reject-event-request"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse> rejectEventRequest(HttpServletRequest request,
                                                               @RequestBody ChangeEventRequest changeEventRequest) {
        String methodName = "rejectEventRequest";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        eventContactService.changeStatusEventMemberRequest(changeEventRequest, Constant.TypeConstant.EventMemberStatus.REJECT);
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/accept-event-request/{token}"}, method = RequestMethod.GET)
    public void acceptEventRequestWithToken(HttpServletResponse response, @PathVariable String token) {
        String methodName = "acceptEventRequestWithToken";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        LOGGER.info("token " + token);
        try {
            String link = eventContactService.changeStatusEventMemberRequestWithToken(token, Constant.TypeConstant.EventMemberStatus.ACCEPT);
            response.sendRedirect(link);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
    }

    @RequestMapping(value = {"/reject-event-request/{token}"}, method = RequestMethod.GET)
    public void rejectEventRequestWithToken(HttpServletResponse response, @PathVariable String token) {
        String methodName = "rejectEventRequestWithToken";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        LOGGER.info("token " + token);
        try {
            String link = eventContactService.changeStatusEventMemberRequestWithToken(token, Constant.TypeConstant.EventMemberStatus.REJECT);
            response.sendRedirect(link);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
    }


    @RequestMapping(value = {"/search-user-to-add"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<ContactShortResponse>>> searchUserToAdd(HttpServletRequest request,
                                                                                        @RequestBody SearchUserToAddToEvent input) {
        String methodName = "searchUserToAdd";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(eventContactService.searchUserToAddToEvent(input, creatorEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventContactResponse>> getById(@PathVariable Long id) {
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(eventContactService.getById(id));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
}
