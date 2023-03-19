package com.ems.api.service.impl;

import com.ems.api.dto.request.BoothOrderRequest;
import com.ems.api.dto.request.SendMailRequest;
import com.ems.api.dto.request.TicketOrderRequest;
import com.ems.api.dto.response.BoothOrderResponse;
import com.ems.api.dto.response.GroupTicketOrderDetailResponse;
import com.ems.api.dto.response.PublicTicketResponse;
import com.ems.api.dto.response.TicketOrderResponse;
import com.ems.api.entity.*;
import com.ems.api.repository.*;
import com.ems.api.service.AddressService;
import com.ems.api.service.EmailService;
import com.ems.api.service.OrderService;
import com.ems.api.service.TicketService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = AppException.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AddressService addressService;
    @Autowired
    private TicketOrderRepo ticketOrderRepo;
    @Autowired
    private TicketOrderDetailRepo ticketOrderDetailRepo;
    @Autowired
    private TicketRepo ticketRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private BoothOrderRepo boothOrderRepo;
    @Autowired
    private EmailService emailService;
    @Value("${logo.path}")
    private String logoPath;

    @Override
    public TicketOrderResponse orderTicket(TicketOrderRequest request) throws AppException {
//        Long addressId = request.getAddressRequest().getId();
        TicketOrder newOrder = new TicketOrder();

        Address address = addressService.create(request.getAddressRequest());
        newOrder.setAddressId(address.getId());

        newOrder.setAmount(request.getAmount());
        newOrder.setQuantity(request.getOrderDetailRequests().size());
        newOrder.setName(request.getName());
        newOrder.setEmail(request.getEmail());
        newOrder.setPhone(request.getPhone());
        newOrder.setEventId(request.getEventId());
        newOrder = save(newOrder);
        final Long orderId = newOrder.getId();
        Map<Long, Integer> orderQuantityByTicketIdMap = new HashMap<>();
        List<TicketOrderDetail> ticketOrderDetails = request.getOrderDetailRequests().stream().map(d -> {
            TicketOrderDetail tod = modelMapper.map(d, TicketOrderDetail.class);
            if (orderQuantityByTicketIdMap.containsKey(tod.getTicketId())) {
                orderQuantityByTicketIdMap.put(tod.getTicketId(), orderQuantityByTicketIdMap.get(tod.getTicketId()) + 1);
            } else {
                orderQuantityByTicketIdMap.put(tod.getTicketId(), 1);
            }
            tod.setTicketOrderId(orderId);
            tod.setId(null);
            return tod;
        }).collect(Collectors.toList());
        List<AppException> exceptions = new ArrayList<>();
        for (Map.Entry<Long, Integer> eMap : orderQuantityByTicketIdMap.entrySet()) {
            Long ticketId = eMap.getKey();
            Integer orderedQuantity = eMap.getValue();
            try {
                ticketService.orderTicket(ticketId, orderedQuantity);
            } catch (AppException e) {
                exceptions.add(e);
            }
        }
        if (!exceptions.isEmpty()) {
            delete(newOrder);
            throw new AppException(Constant.ErrorCode.NOT_ENOUGH_REMAINING_TICKET.name(),
                    exceptions.stream().map(ex -> ex.getMessage()).collect(Collectors.toList()).stream().collect(Collectors.joining(",")));
        }
        ticketOrderDetails = ticketOrderDetailRepo.saveAll(ticketOrderDetails);
        newOrder.setCode(eventRepo.getOne(request.getEventId()).getCode() + "_" + newOrder.getId());
        newOrder = save(newOrder);
        TicketOrderResponse response = new TicketOrderResponse();
        response.setTicketOrder(newOrder);
        response.setTicketOrderDetails(ticketOrderDetails);
        return response;
    }

    @Override
    public BoothOrderResponse orderBooth(BoothOrderRequest request) throws AppException {
        if (boothOrderRepo.existsByBoothIdAndEmailAndStatus(request.getBoothId(), request.getEmail(), Constant.TypeConstant.BoothOrderStatus.WAITING)) {
            throw new AppException(Constant.ErrorCode.THIS_EMAIL_HAS_WAITING_BOOTH_ORDER.name());
        }
        BoothOrder order = modelMapper.map(request, BoothOrder.class);
        order.setStatus(Constant.TypeConstant.BoothOrderStatus.WAITING);
        return modelMapper.map(boothOrderRepo.save(order), BoothOrderResponse.class);
    }

    @Override
    public void sendTicketOrder(TicketOrderResponse orderResponse) throws AppException {
        Event event = eventRepo.getOne(orderResponse.getTicketOrder().getEventId());
        Map<Long, Ticket> tickets = ticketRepo.findAllByEventId(event.getId()).stream().collect(Collectors.toMap(Ticket::getId, Function.identity()));
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setTo(Collections.singletonList(orderResponse.getTicketOrder().getEmail()));
        sendMailRequest.setEmailType(Constant.EmailType.BUY_TICKET_EVENT);
        Map<String, Object> rawData = new HashMap<>();
        rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, orderResponse.getTicketOrder().getName());
        rawData.put(Constant.EmailVariable.CONTENT.value, event.getContentBuyTicketMail());
        rawData.put(Constant.EmailVariable.LOGO_URL.value, logoPath);
        rawData.put(Constant.EmailVariable.QUANTITY.value, orderResponse.getTicketOrder().getQuantity());
        rawData.put(Constant.EmailVariable.AMOUNT.value, orderResponse.getTicketOrder().getAmount());
        Map<Long,GroupTicketOrderDetailResponse> ticketOrdersMap = new HashMap<>();
        orderResponse.getTicketOrderDetails().stream().forEach(tod -> {
                    if (!ticketOrdersMap.containsKey(tod.getTicketId())) {
                        GroupTicketOrderDetailResponse groupTicket = new GroupTicketOrderDetailResponse();
                        groupTicket.setName(tickets.get(tod.getTicketId()).getName());
                        groupTicket.setPrice(tickets.get(tod.getTicketId()).getPrice());
                        groupTicket.setQuantity(1);
                        ticketOrdersMap.put(tod.getTicketId(), groupTicket);
                    } else {
                        GroupTicketOrderDetailResponse exitGroupTicket = ticketOrdersMap.get(tod.getTicketId());
                        exitGroupTicket.setQuantity(exitGroupTicket.getQuantity()+1);
                        ticketOrdersMap.put(tod.getTicketId(),  exitGroupTicket);
                    }
                }
        );
        rawData.put(Constant.EmailVariable.TICKET_ORDERS.value, ticketOrdersMap);
        sendMailRequest.setRawData(rawData);
        emailService.composeEmail(Collections.singletonList(sendMailRequest));
    }

    @Transactional
    TicketOrder save(TicketOrder o) {
        return ticketOrderRepo.save(o);
    }

    @Transactional
    void delete(TicketOrder o) {
        ticketOrderRepo.delete(o);
    }
}
