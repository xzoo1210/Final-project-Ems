package com.ems.api.controller;

import com.ems.api.dto.request.AgendaRequest;
import com.ems.api.dto.response.AgendaResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.service.AgendaService;
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

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.AGENDA)
public class AgendaController {
    protected final Logger LOGGER = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private AgendaService agendaService;
    @Autowired
    private EventContactService eventContactService;

    @RequestMapping(value="create", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<AgendaResponse>> create(@RequestBody AgendaRequest request){
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), creator,
                    Constant.TypeConstant.EventFeature.AGENDA, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(agendaService.create(request, creator));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
    @RequestMapping(value="update", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<AgendaResponse>> update(@RequestBody AgendaRequest request){
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), creator,
                    Constant.TypeConstant.EventFeature.AGENDA, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(agendaService.update(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
    @RequestMapping(value="get-all-in-event/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<AgendaResponse>>> getAllInEvent(@PathVariable Long eventId){
        String methodName = "getAllInEvent";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, creator,
                    Constant.TypeConstant.EventFeature.AGENDA, Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(agendaService.getAllInEvent(eventId));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
    @RequestMapping(value="{eventId}/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<AgendaResponse>>> getById(@PathVariable Long eventId,@PathVariable Long id){
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, creator,
                    Constant.TypeConstant.EventFeature.AGENDA, Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(agendaService.getById(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
    @RequestMapping(value="/delete/{eventId}/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<AgendaResponse>>> delete(@PathVariable Long eventId,@PathVariable Long id){
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, creator,
                    Constant.TypeConstant.EventFeature.AGENDA, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(agendaService.delete(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
}
