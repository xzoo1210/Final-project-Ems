package com.ems.api.controller;

import com.ems.api.entity.Event;
import com.ems.api.entity.Ticket;
import com.ems.api.entity.TicketOrder;
import com.ems.api.entity.TicketOrderDetail;
import com.ems.api.dto.request.*;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.PublicTicketResponse;
import com.ems.api.dto.response.SearchResponse;
import com.ems.api.dto.response.SearchTicketResponse;
import com.ems.api.service.EventContactService;
import com.ems.api.service.TicketService;
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
import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.TICKET)
public class TicketController {
    protected final Logger LOGGER = LoggerFactory.getLogger(TicketController.class);
    @Autowired
    private TicketService ticketService;
    @Autowired
    private EventContactService eventContactService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Ticket>> create(HttpServletRequest request, @RequestBody CreateTicketRequest input) {
        String methodName = "create";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    input.getEventId(),
                    creator,
                    Constant.TypeConstant.EventFeature.TICKET,
                    Constant.TypeConstant.EventMemberAccess.EDIT
            );
            messagesResponse.setData(ticketService.create(input, creator));
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
    public ResponseEntity<MessagesResponse<Ticket>> update(HttpServletRequest request, @RequestBody UpdateTicketRequest input) {
        String methodName = "update";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    input.getEventId(),
                    creator,
                    Constant.TypeConstant.EventFeature.TICKET,
                    Constant.TypeConstant.EventMemberAccess.EDIT
            );
            messagesResponse.setData(ticketService.update(input, creator));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "find-by-event", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchTicketResponse>> findByEventId(HttpServletRequest request, @RequestBody SearchTicketRequest input) {
        String methodName = "findByEventId";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String creator = SecurityContextHolder.getContext().getAuthentication().getName();
            messagesResponse.setData(ticketService.findByEventId(input));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "find-all-by-event/{eventId}", method = RequestMethod.GET)
    public ResponseEntity<MessagesResponse<List<PublicTicketResponse>>> findAllByEventId(HttpServletRequest request, @PathVariable Long eventId) {
        String methodName = "findAllByEventId";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(ticketService.findAllByEventId(eventId));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Ticket>> getById(HttpServletRequest request, @PathVariable Long id) {
        String methodName = "getById";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(ticketService.getById(id));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "orders", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<SearchResponse<TicketOrder>>> searchOrder(HttpServletRequest request, @RequestBody SearchTicketOrderRequest input) throws AppException, InterruptedException {
        String methodName = "searchOrder";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        String creator = SecurityContextHolder.getContext().getAuthentication().getName();
        eventContactService.checkAuthorInEvent(
                input.getEventId(),
                creator,
                Constant.TypeConstant.EventFeature.TICKET,
                Constant.TypeConstant.EventMemberAccess.VIEW
        );
        messagesResponse.setData(ticketService.searchOrder(input));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "order/{ticketOrderId}", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<List<TicketOrderDetail>>> getAllTicketOrderDetails(@PathVariable Long ticketOrderId) throws AppException, InterruptedException {
        String methodName = "getAllTicketOrderDetails";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_BEGIN, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        messagesResponse.setData(ticketService.getAllTicketOrderDetails(ticketOrderId));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "change-status-sale-ticket/{eventId}",method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<Event>> changeStatusSaleTicket(@PathVariable Long eventId) {
        String methodName = "changeStatusSaleTicket";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            eventContactService.checkAuthorInEvent(
                    eventId,
                    userEmail,
                    Constant.TypeConstant.EventFeature.TICKET,
                    Constant.TypeConstant.EventMemberAccess.EDIT
            );
            messagesResponse.setData(ticketService.changeStatusSaleTicket(eventId));
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "get-sale-ticket/{eventId}",method = RequestMethod.GET)
    public ResponseEntity<MessagesResponse<Constant.TypeConstant.SaleTicket>> getStatusSaleTicket(@PathVariable Long eventId) {
        String methodName = "getStatusSaleTicket";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
            messagesResponse.setData(ticketService.getStatusSaleTicket(eventId));
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }
}
