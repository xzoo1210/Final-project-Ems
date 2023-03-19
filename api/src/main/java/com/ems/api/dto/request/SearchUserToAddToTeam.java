package com.ems.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchUserToAddToTeam {
    private String keyword;
    private Long teamId;
}
