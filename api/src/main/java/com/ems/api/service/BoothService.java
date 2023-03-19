package com.ems.api.service;

import com.ems.api.dto.response.*;
import com.ems.api.entity.Event;
import com.ems.api.dto.request.*;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;

public interface BoothService {
    BoothLocationResponse createLocation(BoothLocationRequest input, String creatorEmail) throws AppException;

    BoothLocationResponse updateLocation(BoothLocationUpdateRequest input) throws AppException;

    BoothLocationResponse deleteLocation(Long id) throws AppException;

    BoothResponse create(BoothRequest input, String creatorEmail) throws AppException;

    BoothResponse update(BoothUpdateRequest input) throws AppException;

    BoothResponse delete(Long id) throws AppException;

    SearchResponse<BoothLocationResponse> searchLocation(SearchBoothRequest request);

    SearchResponse<BoothResponse> search(SearchBoothRequest request);

    SearchResponse<PublicBoothResponse> getAllBoothInEvent(SearchBoothRequest eventId);

    Event saveMapPath(SaveMapRequest request);

    String getMapPath(Long eventId);

    Event changeStatusRentBooth(Long eventId);

    Constant.TypeConstant.RentBooth getStatusRentBooth(Long eventId);

    SearchResponse<BoothOrderResponse> searchOrder(SearchBoothOrderRequest request);

    BoothOrderResponse changeOrderBoothStatus(Long id, Constant.TypeConstant.BoothOrderStatus status) throws AppException;
}
