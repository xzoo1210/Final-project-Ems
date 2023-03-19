package com.ems.api.service;

import com.ems.api.dto.request.SearchSponsorRequest;
import com.ems.api.dto.request.SponsorRequest;
import com.ems.api.dto.response.PublicSponsorResponse;
import com.ems.api.dto.response.SearchResponse;
import com.ems.api.dto.response.SponsorResponse;
import com.ems.api.util.AppException;

import java.util.List;

public interface SponsorService {
    SponsorResponse create(SponsorRequest request, String creator) throws AppException;

    SponsorResponse update(SponsorRequest request) throws AppException;

    SearchResponse<SponsorResponse> getAllInEvent(SearchSponsorRequest request) throws AppException;

    List<PublicSponsorResponse> getAllPublicInEvent(Long eventId);

    SponsorResponse delete(Long id);
}
