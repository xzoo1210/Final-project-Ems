package com.ems.api.service;

import com.ems.api.dto.request.BoothOrderRequest;
import com.ems.api.dto.request.TicketOrderRequest;
import com.ems.api.dto.response.BoothOrderResponse;
import com.ems.api.dto.response.TicketOrderResponse;
import com.ems.api.util.AppException;

public interface OrderService {
    TicketOrderResponse orderTicket(TicketOrderRequest input) throws AppException;

    BoothOrderResponse orderBooth(BoothOrderRequest request) throws AppException;

    void sendTicketOrder(TicketOrderResponse orderResponse) throws AppException;
}
