package com.ems.api.service.impl;

import com.ems.api.dto.request.*;
import com.ems.api.dto.response.*;
import com.ems.api.entity.Booth;
import com.ems.api.entity.BoothLocation;
import com.ems.api.entity.BoothOrder;
import com.ems.api.entity.Event;
import com.ems.api.repository.*;
import com.ems.api.service.BoothService;
import com.ems.api.service.EmailService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoothServiceImpl implements BoothService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BoothRepo boothRepo;
    @Autowired
    private BoothLocationRepo boothLocationRepo;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private BoothOrderRepo boothOrderRepo;
    @Autowired
    private EmailService emailService;
    @Value("${logo.path}")
    private String logoPath;

    @Override
    public BoothLocationResponse createLocation(BoothLocationRequest input, String creatorEmail) throws AppException {
        BoothLocation boothLocation = modelMapper.map(input, BoothLocation.class);
        boothLocation.setId(null);
        boothLocation.setCreatorId(contactRepo.findByAccountEmail(creatorEmail).getId());
        return modelMapper.map(boothLocationRepo.save(boothLocation), BoothLocationResponse.class);
    }

    @Override
    public BoothLocationResponse updateLocation(BoothLocationUpdateRequest input) throws AppException {
        BoothLocation boothLocation = boothLocationRepo.getOne(input.getId());
        boothLocation.setName(input.getName());
        boothLocation.setDescription(input.getDescription());
        return modelMapper.map(boothLocationRepo.save(boothLocation), BoothLocationResponse.class);
    }

    @Override
    public BoothLocationResponse deleteLocation(Long id) throws AppException {
        BoothLocation boothLocation = boothLocationRepo.getOne(id);
        if (boothRepo.existsByLocationId(id))
            throw new AppException(Constant.ErrorCode.LOCATION_IN_USE.name());
        boothLocationRepo.deleteById(id);
        return modelMapper.map(boothLocation, BoothLocationResponse.class);
    }

    @Override
    public BoothResponse create(BoothRequest input, String creatorEmail) throws AppException {
        Booth booth = modelMapper.map(input, Booth.class);
        booth.setCreatorId(contactRepo.findByAccountEmail(creatorEmail).getId());
        booth.setStatus(Constant.TypeConstant.BoothStatus.ACTIVE);
        return modelMapper.map(boothRepo.save(booth), BoothResponse.class);
    }

    @Override
    public BoothResponse update(BoothUpdateRequest input) throws AppException {
        Booth booth = boothRepo.getOne(input.getId());
        //TODO check if booth is rented -> not allow disable
        if (Constant.TypeConstant.BoothStatus.NOT_ACTIVE.equals(input.getStatus()) &&
                Constant.TypeConstant.BoothStatus.ACTIVE.equals(booth.getStatus())) {
            if (boothOrderRepo.existsByBoothId(input.getId()))
                throw new AppException(Constant.ErrorCode.NOT_ALLOW_CHANGE_STATUS_BOOTH_HAS_ORDERS.name());
        }
        if (!booth.getLocationId().equals(input.getLocationId())) {
            if (boothOrderRepo.existsByBoothId(input.getId()))
                throw new AppException(Constant.ErrorCode.NOT_ALLOW_CHANGE_LOCATION_BOOTH_HAS_ORDERS.name());
        }
        booth.setStatus(input.getStatus());
        booth.setLocationId(input.getLocationId());
        booth.setName(input.getName());
        booth.setDescription(input.getDescription());
        booth.setStartRentDate(input.getStartRentDate());
        booth.setEndRentDate(input.getEndRentDate());
        return modelMapper.map(boothRepo.save(booth), BoothResponse.class);
    }

    @Override
    public BoothResponse delete(Long id) throws AppException {
        Booth booth = boothRepo.getOne(id);
        if (boothOrderRepo.existsByBoothId(id))
            throw new AppException(Constant.ErrorCode.NOT_ALLOW_DELETE_BOOTH_HAS_ORDERS.name());
        boothRepo.deleteById(id);
        return modelMapper.map(booth, BoothResponse.class);
    }

    @Override
    public SearchResponse<BoothLocationResponse> searchLocation(SearchBoothRequest request) {
        SearchResponse<BoothLocationResponse> response = new SearchResponse<>();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<BoothLocation> boothLocationPage = boothLocationRepo.findAllByEventId(request.getEventId(), pageableRequested);
        List<BoothLocationResponse> foundLocations = boothLocationPage.getContent().stream().map(
                l -> modelMapper.map(l, BoothLocationResponse.class)
        ).collect(Collectors.toList());
        response.setPage(new PageImpl<>(foundLocations, pageableRequested, boothLocationPage.getTotalElements()));
        return response;
    }

    @Override
    public SearchResponse<BoothResponse> search(SearchBoothRequest request) {
        SearchResponse<BoothResponse> response = new SearchResponse<>();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Booth> boothPage = boothRepo.findAllByEventId(request.getEventId(), pageableRequested);
        List<BoothResponse> foundLocations = boothPage.getContent().stream().map(
                b -> {
                    BoothResponse br = modelMapper.map(b, BoothResponse.class);
                    br.setLocation(modelMapper.map(b.getLocation(), BoothLocationResponse.class));
                    br.setIsRented(boothOrderRepo.existsByBoothIdAndStatus(b.getId(), Constant.TypeConstant.BoothOrderStatus.ACCEPT));
                    return br;
                }
        ).collect(Collectors.toList());
        response.setPage(new PageImpl<>(foundLocations, pageableRequested, boothPage.getTotalElements()));
        return response;
    }

    @Override
    public SearchResponse<PublicBoothResponse> getAllBoothInEvent(SearchBoothRequest request) {
        SearchResponse<PublicBoothResponse> response = new SearchResponse<>();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Booth> boothPage = boothRepo.findAllByEventId(request.getEventId(), pageableRequested);
        List<PublicBoothResponse> foundLocations = boothPage.getContent().stream().filter(
                b -> Constant.TypeConstant.BoothStatus.ACTIVE.equals(b.getStatus())
        ).map(
                b -> {
                    PublicBoothResponse br = modelMapper.map(b, PublicBoothResponse.class);
                    br.setLocation(modelMapper.map(b.getLocation(), BoothLocationResponse.class));
                    return br;
                }
        ).collect(Collectors.toList());
        response.setPage(new PageImpl<>(foundLocations, pageableRequested, boothPage.getTotalElements()));
        return response;
    }

    @Override
    public Event saveMapPath(SaveMapRequest request) {
        Event event = eventRepo.getOne(request.getEventId());
        event.setMapPath(request.getMapPath());
        return eventRepo.save(event);
    }

    @Override
    public String getMapPath(Long eventId) {
        return eventRepo.getOne(eventId).getMapPath();
    }

    @Override
    public Event changeStatusRentBooth(Long eventId) {
        Event event = eventRepo.getOne(eventId);
        switch (event.getRentBooth()) {
            case ENABLE:
                event.setRentBooth(Constant.TypeConstant.RentBooth.DISABLE);
                break;
            case DISABLE:
                event.setRentBooth(Constant.TypeConstant.RentBooth.ENABLE);
                break;
        }
        return eventRepo.save(event);
    }

    @Override
    public Constant.TypeConstant.RentBooth getStatusRentBooth(Long eventId) {
        return eventRepo.getOne(eventId).getRentBooth();
    }

    @Override
    public SearchResponse<BoothOrderResponse> searchOrder(SearchBoothOrderRequest request) {
        SearchResponse<BoothOrderResponse> response = new SearchResponse<>();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<BoothOrder> boothOrderPage = boothOrderRepo.searchBoothOrder(request.getEventId(), request.getBoothId(), pageableRequested);
        List<BoothOrderResponse> foundList = boothOrderPage.getContent().stream().map(
                bo -> {
                    BoothOrderResponse bor = modelMapper.map(bo, BoothOrderResponse.class);
                    return bor;
                }
        ).collect(Collectors.toList());
        response.setPage(new PageImpl<>(foundList, pageableRequested, boothOrderPage.getTotalElements()));
        return response;
    }

    @Override
    public BoothOrderResponse changeOrderBoothStatus(Long id, Constant.TypeConstant.BoothOrderStatus status) throws AppException {
        BoothOrder order = boothOrderRepo.getOne(id);
        if (!Constant.TypeConstant.BoothOrderStatus.WAITING.equals(order.getStatus())) {
            throw new AppException(Constant.ErrorCode.NOT_ALLOW_CHANGE_STATUS_BOOTH_ORDER.name());
        }
        order.setStatus(status);
        SendMailRequest sendMailRequest = new SendMailRequest();
        sendMailRequest.setTo(Collections.singletonList(order.getEmail()));
        switch (status) {
            case ACCEPT:
                if(boothOrderRepo.existsByBoothIdAndStatus(id, Constant.TypeConstant.BoothOrderStatus.ACCEPT))
                    throw new AppException(Constant.ErrorCode.BOOTH_IS_ALREADY_ACCEPTED.name());
                sendMailRequest.setEmailType(Constant.EmailType.ACCEPT_BOOTH_ORDER);
                break;
            case REJECT:
                sendMailRequest.setEmailType(Constant.EmailType.REJECT_BOOTH_ORDER);
                break;
        }
        Map<String, Object> rawData = new HashMap<>();
        rawData.put(Constant.EmailVariable.LOGO_URL.value, logoPath);
        rawData.put(Constant.EmailVariable.RECEIVER_NAME.value, order.getName());
        Booth booth = order.getBooth();
        rawData.put(Constant.EmailVariable.EVENT_NAME.value, booth.getEvent().getName());
        rawData.put(Constant.EmailVariable.BOOTH_NAME.value, booth.getName());
        rawData.put(Constant.EmailVariable.BOOTH_DESCRIPTION.value, booth.getDescription());
        sendMailRequest.setRawData(rawData);
        emailService.composeEmail(Collections.singletonList(sendMailRequest));
        return modelMapper.map(boothOrderRepo.save(order), BoothOrderResponse.class);
    }
}
