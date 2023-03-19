package com.ems.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "TICKET_ORDER_DETAIL")
public class TicketOrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TICKET_ORDER_DETAIL_SEQ")
    @SequenceGenerator(sequenceName = "TICKET_ORDER_DETAIL_SEQ", allocationSize = 1, name = "TICKET_ORDER_DETAIL_SEQ")
    private Long id;

    private Long ticketOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId", insertable = false, updatable = false)
    @JsonIgnore
    private Ticket ticket;
    private Long ticketId;

    private String name;
    private String email;
    private String phone;

    @JsonProperty("ticketName")
    public String getTicketName() {
        Ticket linkedTicket = this.getTicket();
        return linkedTicket != null ? linkedTicket.getName() : null;
    }

    @JsonProperty("ticketPrice")
    public Double getTicketPrice() {
        Ticket linkedTicket = this.getTicket();
        return linkedTicket != null ? linkedTicket.getPrice() : null;
    }
}
