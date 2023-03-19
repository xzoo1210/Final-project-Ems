package com.ems.api.service;

import com.ems.api.entity.Event;
import com.ems.api.entity.Ticket;
import com.ems.api.entity.TicketOrder;
import com.ems.api.entity.TicketOrderDetail;
import com.ems.api.dto.request.CreateTicketRequest;
import com.ems.api.dto.request.SearchTicketOrderRequest;
import com.ems.api.dto.request.SearchTicketRequest;
import com.ems.api.dto.request.UpdateTicketRequest;
import com.ems.api.dto.response.PublicTicketResponse;
import com.ems.api.dto.response.SearchResponse;
import com.ems.api.dto.response.SearchTicketResponse;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;

import java.util.List;

public interface TicketService {
    Ticket create(CreateTicketRequest input, String creator) throws AppException;

    Ticket update(UpdateTicketRequest input, String creator) throws AppException;

    SearchTicketResponse findByEventId(SearchTicketRequest request) throws AppException;

    List<PublicTicketResponse> findAllByEventId(Long eventId);

    Ticket getById(Long id);

    Ticket orderTicket(Long ticketId, Integer orderedQuantity) throws AppException;
    Ticket inviteTicket(Long ticketId) throws AppException;

    SearchResponse<TicketOrder> searchOrder(SearchTicketOrderRequest input);

    List<TicketOrderDetail> getAllTicketOrderDetails(Long ticketOrderId);

    Event changeStatusSaleTicket(Long eventId);
    Constant.TypeConstant.SaleTicket getStatusSaleTicket(Long eventId);
}
