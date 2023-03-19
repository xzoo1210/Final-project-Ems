package com.ems.api.controller;


import com.ems.api.dto.request.SearchSponsorRequest;
import com.ems.api.dto.request.SponsorRequest;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.PublicSponsorResponse;
import com.ems.api.dto.response.SponsorResponse;
import com.ems.api.service.EventContactService;
import com.ems.api.service.SponsorService;
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
@RequestMapping(Constant.ControllerMapping.SPONSOR)
public class SponsorController {
    protected final Logger LOGGER = LoggerFactory.getLogger(SponsorController.class);
    @Autowired
    private SponsorService sponsorService;
    @Autowired
    private EventContactService eventContactService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SponsorResponse>> create(@RequestBody SponsorRequest request) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), creator,
                    Constant.TypeConstant.EventFeature.SPONSOR, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(sponsorService.create(request, creator));
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
    public ResponseEntity<MessagesResponse<SponsorResponse>> update(@RequestBody SponsorRequest request) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), creator,
                    Constant.TypeConstant.EventFeature.SPONSOR, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(sponsorService.update(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
    @RequestMapping(value = "/delete/{eventId}/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SponsorResponse>> delete(@PathVariable Long eventId,
                                                                    @PathVariable Long id) {
        String methodName = "delete";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, creator,
                    Constant.TypeConstant.EventFeature.SPONSOR, Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(sponsorService.delete(id));
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
    public ResponseEntity<MessagesResponse<List<SponsorResponse>>> getAllInEvent(@RequestBody SearchSponsorRequest request) {
        String methodName = "getAllInEvent";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), creator,
                    Constant.TypeConstant.EventFeature.SPONSOR, Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(sponsorService.getAllInEvent(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "get-all-public-in-event/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<MessagesResponse<List<PublicSponsorResponse>>> getAllPublicInEvent(@PathVariable Long eventId) {
        String methodName = "getAllPublicInEvent";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(sponsorService.getAllPublicInEvent(eventId));

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
}
