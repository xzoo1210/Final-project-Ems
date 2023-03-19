package com.ems.api.controller;

import com.ems.api.entity.Participant;
import com.ems.api.dto.request.BoothOrderRequest;
import com.ems.api.dto.request.TicketOrderRequest;
import com.ems.api.dto.response.BoothOrderResponse;
import com.ems.api.dto.response.MessagesResponse;
import com.ems.api.dto.response.TicketOrderResponse;
import com.ems.api.service.OrderService;
import com.ems.api.service.ParticipantService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@CrossOrigin(origins = "*")
@RequestMapping(Constant.ControllerMapping.ORDER)
public class OrderController {
    protected final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;
    @Autowired
    private ParticipantService participantService;

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<TicketOrderResponse>> orderTicket(@RequestBody TicketOrderRequest request) {
        String methodName = "orderTicket";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            TicketOrderResponse orderResponse = orderService.orderTicket(request);
            List<Participant> participants=participantService.addParticipantFromOrder(orderResponse);
//            participantService.sendTicketOrder(participants);
            orderService.sendTicketOrder(orderResponse);
            participantService.sendTicketToParticipants(participants);
            messagesResponse.setData(orderResponse);
        } catch (AppException ex) {
            LOGGER.error(ex.getMessage(), ex);
            messagesResponse.setMessages(ex.getMessage());
            messagesResponse.setErrorCode(ex.getCode());
            return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
        }
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        return new ResponseEntity<>(messagesResponse, HttpStatus.OK);
    }

    @RequestMapping(value = "booth",method = RequestMethod.POST)
    public ResponseEntity<MessagesResponse<BoothOrderResponse>> orderBooth(@RequestBody BoothOrderRequest request){
        String methodName = "orderBooth";
        long startTime = System.currentTimeMillis();
        LogUtil.showLog(LOGGER, LogUtil.LOG_END, methodName, startTime);
        MessagesResponse messagesResponse = new MessagesResponse();
        try {
            BoothOrderResponse orderResponse = orderService.orderBooth(request);
            messagesResponse.setData(orderResponse);
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
