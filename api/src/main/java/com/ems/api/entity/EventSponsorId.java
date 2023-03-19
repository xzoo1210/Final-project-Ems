package com.ems.api.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Access;
import javax.persistence.AccessType;
import java.io.Serializable;

@Component
@Access(AccessType.FIELD)
@Getter
@Setter
public class EventSponsorId implements Serializable {
    private Long eventId;
    private Long sponsorId;
}
