package com.ems.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchParticipantRequest extends PagingRequest{
    private Long eventId;
    private String keySearch;
    private Long ticketId;
}
