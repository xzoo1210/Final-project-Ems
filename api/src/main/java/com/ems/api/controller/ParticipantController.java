package com.ems.api.controller;

import com.ems.api.dto.request.ParticipantRequest;
import com.ems.api.dto.request.SearchParticipantRequest;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.ParticipantResponse;
import com.ems.api.dto.response.SearchResponse;
import com.ems.api.service.EventContactService;
import com.ems.api.service.ParticipantService;
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

import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.PARTICIPANT)
public class ParticipantController {
    protected final Logger LOGGER = LoggerFactory.getLogger(ParticipantController.class);
    @Autowired
    private ParticipantService paricipantService;
    @Autowired
    private EventContactService eventContactService;


    @RequestMapping(value="search", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchResponse<ParticipantResponse>>> search(@RequestBody SearchParticipantRequest request){
        String methodName = "search";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), userEmail,
                    Constant.TypeConstant.EventFeature.PARTICIPANT, Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(paricipantService.search(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value="invite-participant", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<ParticipantResponse>> inviteParticipant(@RequestBody ParticipantRequest request){
        String methodName = "inviteParticipant";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), userEmail,
                    Constant.TypeConstant.EventFeature.PARTICIPANT, Constant.TypeConstant.EventMemberAccess.EDIT);
            ParticipantResponse participant = paricipantService.inviteParticipant(request,userEmail);
            paricipantService.sendInviteParticipantMail(participant.getId());
            messagesResponse.setData(participant);
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value="/confirm-joined/{eventId}/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<ParticipantResponse>> confirmJoined(@PathVariable Long eventId,
                                                                               @PathVariable Long id) {
        String methodName = "confirmJoined";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, userEmail,
                    Constant.TypeConstant.EventFeature.PARTICIPANT, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(paricipantService.confirmJoined(id,userEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-file/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<byte[]> exportParticipants(HttpServletResponse response, @PathVariable Long eventId) {
        String methodName = "exportParticipants";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
//        byte[] returnFileBytes = new byte[0];
        ResponseEntity result = null;
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, userEmail,
                    Constant.TypeConstant.EventFeature.PARTICIPANT, Constant.TypeConstant.EventMemberAccess.VIEW);

            result = paricipantService.getFile(eventId);

        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);

        return result;
    }
//    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
//    public ResponseEntity test(HttpServletResponse response, @PathVariable Long id) {
//
//
//
//        return ResponseEntity.ok(paricipantService.getById(id));
//    }

}
