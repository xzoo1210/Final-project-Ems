package com.ems.api.dto.response;

import com.ems.api.entity.Ticket;
import lombok.Data;
import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchTicketResponse {
    private Page<Ticket> ticketPage;
}
