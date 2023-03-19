package com.ems.api.controller;

import com.ems.api.dto.request.ChangeEventStatusRequest;
import com.ems.api.dto.request.UpdateMailTemplate;
import com.ems.api.entity.Event;
import com.ems.api.dto.request.CreateEventRequest;
import com.ems.api.dto.request.SearchEventRequest;
import com.ems.api.dto.response.EventResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.SearchEventResponse;
import com.ems.api.service.EventContactService;
import com.ems.api.service.EventService;
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
@RequestMapping(Constant.ControllerMapping.EVENT)
public class EventController {
    protected final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    @Autowired
    private EventService eventService;
    @Autowired
    private EventContactService eventContactService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Event>> create(HttpServletRequest request, @RequestBody CreateEventRequest input) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(eventService.create(input, creator));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Event>> update(@RequestBody CreateEventRequest input) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getId(), user, Constant.TypeConstant.EventFeature.EVENT_DETAIL,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventService.update(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchEventResponse<EventResponse>>> search(@RequestBody SearchEventRequest input) {
        String methodName = "search";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(eventService.search(input, user));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "find-by-code", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Event>> findByCode(@RequestBody SearchEventRequest input) {
        String methodName = "findByCode";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(eventService.findByCode(input.getSearchKey()));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "joined-events-short", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<EventResponse>>> getAllJoinedEventsShort() {
        String methodName = "getAllJoinedEventsShort";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(eventService.getAllJoinedEventsShort(user));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "joined-events", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<Event>>> getAllJoinedEvents() {
        String methodName = "getAllJoinedEvent";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        messagesResponse.setData(eventService.getAllJoinedEventsShort(user));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventResponse>> getById(@PathVariable Long id) {
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(id, user, Constant.TypeConstant.EventFeature.EVENT_DETAIL,
                    Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(eventService.getById(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "update-content-ticket-mail", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventResponse>> updateContentTicketMail(@RequestBody UpdateMailTemplate request) {
        String methodName = "updateContentInvite";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), user, Constant.TypeConstant.EventFeature.EVENT_DETAIL,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventService.updateContentMail(request, Constant.EmailTemplate.TICKET));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "update-content-buy-ticket", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventResponse>> updateContentBuyTicket(@RequestBody UpdateMailTemplate request) {
        String methodName = "updateContentBuyTicket";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), user, Constant.TypeConstant.EventFeature.EVENT_DETAIL,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventService.updateContentMail(request, Constant.EmailTemplate.TICKET_ORDER));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/update-content-invite-participant", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventResponse>> updateContentInviteMail(@RequestBody UpdateMailTemplate request) {
        String methodName = "updateContentInvite";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), user, Constant.TypeConstant.EventFeature.EVENT_DETAIL,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventService.updateContentMail(request, Constant.EmailTemplate.PARTICIPANT));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/change-event-status",method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<EventResponse>> changeEventStatus(@RequestBody ChangeEventStatusRequest request) {
        String methodName = "changeEventStatus";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), userEmail, Constant.TypeConstant.EventFeature.EVENT_DETAIL,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(eventService.changeEventStatus(request, userEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/export-event-information/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<byte[]> exportEventInformation(HttpServletResponse response, @PathVariable Long eventId) {
        String methodName = "exportEventInformation";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
//        byte[] returnFileBytes = new byte[0];
        ResponseEntity result = null;
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, userEmail,
                    Constant.TypeConstant.EventFeature.EVENT_DETAIL, Constant.TypeConstant.EventMemberAccess.EDIT);

            result = eventService.exportEventInformation(eventId);

        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);

        return result;
    }
}
