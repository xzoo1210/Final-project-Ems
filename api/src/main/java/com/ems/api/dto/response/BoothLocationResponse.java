package com.ems.api.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoothLocationResponse extends PublicBoothLocationResponse{
    @JsonBackReference("boothRes")
    private EventResponse event;
    private Date createDate;
    private ContactShortResponse creator;
}
