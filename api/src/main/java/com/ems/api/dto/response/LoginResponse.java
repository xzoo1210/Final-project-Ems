package com.ems.api.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private String jwtToken;
    private Date tokenExpiredAt;
}

