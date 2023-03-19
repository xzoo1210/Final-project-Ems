package com.ems.api.service.impl;

import com.ems.api.entity.Address;
import com.ems.api.dto.request.AddressRequest;
import com.ems.api.repository.AddressRepo;
import com.ems.api.service.AddressService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = AppException.class)
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Address create(AddressRequest request) {
        Address address = modelMapper.map(request, Address.class);
        return addressRepo.save(address);
    }

    @Override
    public Address update(AddressRequest request) throws AppException {
        if (!addressRepo.existsById(request.getId()))
            throw new AppException(Constant.ErrorCode.ADDRESS_ID_NOT_EXIST.name());
        Address address = modelMapper.map(request, Address.class);
        address.setId(address.getId());
        return addressRepo.save(address);
    }

    @Override
    public Address getById(Long id) {
        return addressRepo.findById(id).orElse(null);
    }
}
