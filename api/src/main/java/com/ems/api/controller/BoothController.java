package com.ems.api.controller;


import com.ems.api.dto.request.*;
import com.ems.api.dto.response.*;
import com.ems.api.entity.Event;
import com.ems.api.service.BoothService;
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

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.BOOTH)
public class BoothController {
    protected final Logger LOGGER = LoggerFactory.getLogger(BoothController.class);
    @Autowired
    private BoothService boothService;
    @Autowired
    private EventContactService eventContactService;

    @RequestMapping(value = {"/location/create"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothLocationResponse>> createLocation(HttpServletRequest request,
                                                                                  @RequestBody BoothLocationRequest input) {
        String methodName = "createLocation";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(boothService.createLocation(input, creatorEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/location/update"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothLocationResponse>> updateLocation(HttpServletRequest request,
                                                                                  @RequestBody BoothLocationUpdateRequest input) {
        String methodName = "updateLocation";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(boothService.updateLocation(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"location/delete/{eventId}/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothLocationResponse>> deleteLocation(HttpServletRequest request,
                                                                                  @PathVariable Long id,
                                                                                  @PathVariable Long eventId) {
        String methodName = "deleteLocation";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(boothService.deleteLocation(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/location/search"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchResponse<BoothLocationResponse>>> searchLocation(@RequestBody SearchBoothRequest request) {
        String methodName = "searchLocation";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(boothService.searchLocation(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = {"/create"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothResponse>> create(HttpServletRequest request,
                                                                  @RequestBody BoothRequest input) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(boothService.create(input, creatorEmail));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/update"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothResponse>> update(HttpServletRequest request,
                                                                  @RequestBody BoothUpdateRequest input) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(input.getEventId(), creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(boothService.update(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/delete/{eventId}/{id}"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothResponse>> delete(HttpServletRequest request,
                                                                  @PathVariable Long id,
                                                                  @PathVariable Long eventId) {
        String methodName = "delete";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(eventId, creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT);
            messagesResponse.setData(boothService.delete(id));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/search"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchResponse<BoothResponse>>> search(@RequestBody SearchBoothRequest request) {
        String methodName = "search";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creatorEmail =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(request.getEventId(), creatorEmail, Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.VIEW);
            messagesResponse.setData(boothService.search(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }

        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {"/get-all-in-event"}, method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchResponse<PublicBoothResponse>>> getAllBoothInEvent(@RequestBody SearchBoothRequest request) {
        String methodName = "getAllBoothInEvent";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(boothService.getAllBoothInEvent(request));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = "/save-map-path", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Event>> saveMapPath(@RequestBody SaveMapRequest request) {
        String methodName = "saveMapPath";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    request.getEventId(),
                    userEmail,
                    Constant.TypeConstant.EventFeature.TICKET,
                    Constant.TypeConstant.EventMemberAccess.EDIT
            );
            messagesResponse.setData(boothService.saveMapPath(request));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-map-path/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<MessagesResponse<String>> getMapPath(@PathVariable Long eventId) {
        String methodName = "getMapPath";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(boothService.getMapPath(eventId));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }


    @RequestMapping(value = "change-status-rent-booth/{eventId}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Event>> changeStatusRentBooth(@PathVariable Long eventId) {
        String methodName = "changeStatusRentBooth";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    eventId,
                    userEmail,
                    Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT
            );
            messagesResponse.setData(boothService.changeStatusRentBooth(eventId));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-rent-booth/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<MessagesResponse<Constant.TypeConstant.RentBooth>> getStatusRentBooth(@PathVariable Long eventId) {
        String methodName = "getStatusRentBooth";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(boothService.getStatusRentBooth(eventId));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchResponse<BoothOrderResponse>>> searchOrder(@RequestBody SearchBoothOrderRequest input) {
        String methodName = "searchOrder";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    input.getEventId(),
                    creator,
                    Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.VIEW
            );
            messagesResponse.setData(boothService.searchOrder(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/accept/{eventId}/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothOrderResponse>> acceptBoothOrder(@PathVariable Long eventId,
                                                                                 @PathVariable Long id) {
        String methodName = "acceptBoothOrder";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    eventId,
                    creator,
                    Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT
            );
            messagesResponse.setData(boothService.changeOrderBoothStatus(id, Constant.TypeConstant.BoothOrderStatus.ACCEPT ));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/reject/{eventId}/{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothOrderResponse>> rejectBoothOrder(@PathVariable Long eventId,
                                                                                 @PathVariable Long id) {
        String methodName = "rejectBoothOrder";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    eventId,
                    creator,
                    Constant.TypeConstant.EventFeature.BOOTH,
                    Constant.TypeConstant.EventMemberAccess.EDIT
            );
            messagesResponse.setData(boothService.changeOrderBoothStatus(id, Constant.TypeConstant.BoothOrderStatus.REJECT ));
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
