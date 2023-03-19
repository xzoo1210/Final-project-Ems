package com.ems.api.service.impl;

import com.ems.api.entity.*;
import com.ems.api.dto.request.CreateTicketRequest;
import com.ems.api.dto.request.SearchTicketOrderRequest;
import com.ems.api.dto.request.SearchTicketRequest;
import com.ems.api.dto.request.UpdateTicketRequest;
import com.ems.api.dto.response.PublicTicketResponse;
import com.ems.api.dto.response.SearchResponse;
import com.ems.api.dto.response.SearchTicketResponse;
import com.ems.api.repository.*;
import com.ems.api.service.EventContactService;
import com.ems.api.service.TicketService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = AppException.class)
public class TicketServiceImpl implements TicketService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EventContactService eventContactService;
    @Autowired
    private TicketOrderDetailRepo ticketOrderDetailRepo;
    @Autowired
    private TicketOrderRepo ticketOrderRepo;

    @Override
    public Ticket create(CreateTicketRequest input, String creator) throws AppException {
        Contact creatorContact = contactRepo.findByAccountEmail(creator);
        Ticket newTicket = new Ticket();
        newTicket.setCreatorId(creatorContact.getId());
        newTicket.setName(input.getName());
        newTicket.setLimited(input.getLimited());
        newTicket.setRemainingQuantity(input.getLimited());
        newTicket.setPrice(input.getPrice());
        newTicket.setSaleBeginDate(input.getSaleBeginDate());
        newTicket.setSaleEndDate(input.getSaleEndDate());
        newTicket.setDescription(input.getDescription());
        newTicket.setEventId(input.getEventId());
        newTicket.setStatus(Constant.TypeConstant.TicketStatus.ACTIVE);
        return ticketRepo.save(newTicket);
    }

    @Override
    public Ticket update(UpdateTicketRequest input, String creator) throws AppException {
        Ticket updateTicket = ticketRepo.findByIdForUpdate(input.getId());
        if (input.getLimited() < updateTicket.getSoldQuantity()) {
            throw new AppException(Constant.ErrorCode.LIMITED_QUANTITY_CANNOT_LESS_THAN_SOLD_QUANTITY.name());
        }
        updateTicket.setName(input.getName());
        updateTicket.setLimited(input.getLimited());
        updateTicket.setRemainingQuantity(input.getLimited() - updateTicket.getSoldQuantity());
        updateTicket.setSaleBeginDate(input.getSaleBeginDate());
        updateTicket.setSaleEndDate(input.getSaleEndDate());
        updateTicket.setDescription(input.getDescription());
        updateTicket.setStatus(input.getStatus());
        return ticketRepo.save(updateTicket);
    }

    @Override
    public SearchTicketResponse findByEventId(SearchTicketRequest request) throws AppException {
        SearchTicketResponse searchTicketResponse = new SearchTicketResponse();
        searchTicketResponse.setTicketPage(ticketRepo.findAllByEventId(request.getEventId(),
                PageUtil.createPageRequest(request)));
        return searchTicketResponse;
    }

    @Override
    public List<PublicTicketResponse> findAllByEventId(Long eventId) {
        Date currentDate = new Date();
        Event event = eventRepo.getOne(eventId);
        if (Constant.TypeConstant.SaleTicket.DISABLE.equals(event.getSaleTicket()))
            return Collections.emptyList();
        List<Ticket> tickets = ticketRepo.findAllByEventIdAndStatusOrderByPriceDesc(eventId, Constant.TypeConstant.TicketStatus.ACTIVE);
        return tickets.isEmpty() ? Collections.emptyList() : tickets.stream()
                .map(t -> modelMapper.map(t, PublicTicketResponse.class)).filter(t->t.getSaleEndDate().after(currentDate)).collect(Collectors.toList());
    }

    @Override
    public Ticket getById(Long id) {
        return ticketRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = {AppException.class})
    public Ticket orderTicket(Long ticketId, Integer orderedQuantity) throws AppException {
        Ticket ticket = ticketRepo.findByIdForUpdate(ticketId);
        try {
            if (ticket.getRemainingQuantity().compareTo(orderedQuantity) < 0) {
                throw new AppException(Constant.ErrorCode.NOT_ENOUGH_REMAINING_TICKET.name(), ticket.getName());
            }
            Integer newSoldQuantity = ticket.getSoldQuantity() + orderedQuantity;
            ticket.setSoldQuantity(newSoldQuantity);
            ticket.setRemainingQuantity(ticket.getRemainingQuantity() - orderedQuantity);
        } catch (Exception e) {
            throw new AppException(e, Constant.ErrorCode.SYSTEM_ERROR.name());
        }
        return ticketRepo.save(ticket);
    }

    @Override
    @Transactional(rollbackFor = {AppException.class})
    public Ticket inviteTicket(Long ticketId) throws AppException {
        Ticket ticket = ticketRepo.findByIdForUpdate(ticketId);
        try {
            if (ticket.getRemainingQuantity().compareTo(1) < 0) {
                throw new AppException(Constant.ErrorCode.NOT_ENOUGH_REMAINING_TICKET.name(), ticket.getName());
            }
            ticket.setRemainingQuantity(ticket.getRemainingQuantity() - 1);
        } catch (Exception e) {
            throw new AppException(e, Constant.ErrorCode.SYSTEM_ERROR.name());
        }
        return ticketRepo.save(ticket);
    }

    @Override
    public SearchResponse<TicketOrder> searchOrder(SearchTicketOrderRequest request) {
        SearchResponse<TicketOrder> response = new SearchResponse<>();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        response.setPage(ticketOrderRepo.searchTicketOrder(request.getEventId(),
                request.getKeySearch(), pageableRequested));
        return response;
    }

    @Override
    public List<TicketOrderDetail> getAllTicketOrderDetails(Long ticketOrderId) {
        return ticketOrderDetailRepo.findAllByTicketOrderId(ticketOrderId);
    }

    @Override
    public Event changeStatusSaleTicket(Long eventId) {
        Event event = eventRepo.getOne(eventId);
        switch (event.getSaleTicket()) {
            case ENABLE:
                event.setSaleTicket(Constant.TypeConstant.SaleTicket.DISABLE);
                break;
            case DISABLE:
                event.setSaleTicket(Constant.TypeConstant.SaleTicket.ENABLE);
                break;
        }
        return eventRepo.save(event);
    }

    @Override
    public Constant.TypeConstant.SaleTicket getStatusSaleTicket(Long eventId) {
        return eventRepo.getOne(eventId).getSaleTicket();
    }
}
