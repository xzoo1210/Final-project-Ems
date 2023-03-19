package com.ems.api.dto.response;

import com.ems.api.util.Constant;
import lombok.Data;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorResponse extends PublicSponsorResponse{
    private Long id;
    private Long eventId;
    private String sponsorItem;
    private String moreInformation;
    private Constant.TypeConstant.PublicSponsor publicSponsor;
    private ContactShortResponse creator;
    private Date createDate;
}
