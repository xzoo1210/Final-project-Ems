package com.ems.api.service.impl;

import com.ems.api.entity.Sponsor;
import com.ems.api.dto.request.SearchSponsorRequest;
import com.ems.api.dto.request.SponsorRequest;
import com.ems.api.dto.response.*;
import com.ems.api.repository.ContactRepo;
import com.ems.api.repository.SponsorRepo;
import com.ems.api.service.SponsorService;
import com.ems.api.util.AppException;
import com.ems.api.util.Constant;
import com.ems.api.util.PageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SponsorServiceImpl implements SponsorService {
    @Autowired
    private ContactRepo contactRepo;
    @Autowired
    private SponsorRepo sponsorRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SponsorResponse create(SponsorRequest request, String creator) throws AppException {
        Sponsor sponsor = modelMapper.map(request,Sponsor.class);
        sponsor.setId(null);
        sponsor.setCreatorId(contactRepo.findByAccountEmail(creator).getId());
        return modelMapper.map(sponsorRepo.save(sponsor),SponsorResponse.class);
    }

    @Override
    public SponsorResponse update(SponsorRequest request) throws AppException {
        Sponsor sponsor = sponsorRepo.getOne(request.getId());
        sponsor.setName(request.getName());
        sponsor.setPhone(request.getPhone());
        sponsor.setEmail(request.getEmail());
        sponsor.setSponsorItem(request.getSponsorItem());
        sponsor.setMoreInformation(request.getMoreInformation());
        sponsor.setLink(request.getLink());
        sponsor.setLogoPath(request.getLogoPath());
        sponsor.setPublicSponsor(request.getPublicSponsor());
        return modelMapper.map(sponsorRepo.save(sponsor), SponsorResponse.class);
    }

    @Override
    public SearchResponse<SponsorResponse> getAllInEvent(SearchSponsorRequest request) throws AppException {
        SearchResponse<SponsorResponse>  response = new SearchResponse();
        Pageable pageableRequested = PageUtil.createPageRequest(request);
        Page<Sponsor> foundSponsor = sponsorRepo.search(request.getEventId(),pageableRequested);
        List<SponsorResponse> listSponsor = foundSponsor.getContent().stream()
                .map(s -> {
                    SponsorResponse sr = modelMapper.map(s, SponsorResponse.class);
                    return sr;
                }).collect(Collectors.toList());

        response.setPage(new PageImpl<>(listSponsor, pageableRequested, foundSponsor.getTotalElements()));
        return response;
    }

    @Override
    public List<PublicSponsorResponse> getAllPublicInEvent(Long eventId) {
        return sponsorRepo.findAllByEventIdAndPublicSponsor(eventId,
                Constant.TypeConstant.PublicSponsor.ACTIVE).stream()
                .map(s->modelMapper.map(s,PublicSponsorResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public SponsorResponse delete(Long id) {
        Sponsor sponsor = sponsorRepo.getOne(id);
        sponsorRepo.delete(sponsor);
        return modelMapper.map(sponsor, SponsorResponse.class);
    }
}
