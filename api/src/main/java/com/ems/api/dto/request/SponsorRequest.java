package com.ems.api.dto.request;

import com.ems.api.util.Constant;
import lombok.Data;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorRequest {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String sponsorItem;
    private String moreInformation;
    private String link;

    private String logoPath;
    private Constant.TypeConstant.PublicSponsor publicSponsor;
    private Long eventId;
}
