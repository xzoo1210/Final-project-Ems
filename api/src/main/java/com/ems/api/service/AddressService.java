package com.ems.api.service;

import com.ems.api.entity.Address;
import com.ems.api.dto.request.AddressRequest;
import com.ems.api.util.AppException;

public interface AddressService {
    Address create(AddressRequest request);

    Address update(AddressRequest request) throws AppException;

    Address getById(Long id);
}
